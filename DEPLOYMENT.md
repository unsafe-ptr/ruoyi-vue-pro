# 🚀 部署指南

> **一键部署到生产环境，支持多种部署方式！**

<div align="center">

[![Docker](https://img.shields.io/badge/Docker-推荐-blue.svg)](https://www.docker.com/)
[![Kubernetes](https://img.shields.io/badge/Kubernetes-企业级-green.svg)](https://kubernetes.io/)
[![Linux](https://img.shields.io/badge/Linux-支持-orange.svg)](https://www.linux.org/)

</div>

## ⚡ 快速部署选择

| 部署方式 | 适用场景 | 难度 | 推荐指数 |
|----------|----------|------|----------|
| 🐳 [Docker Compose](#-docker-compose-推荐) | 小型项目、快速部署 | ⭐ | ⭐⭐⭐⭐⭐ |
| 🖥 [传统部署](#-传统部署) | 已有环境、定制需求 | ⭐⭐⭐ | ⭐⭐⭐ |
| ☸️ [Kubernetes](#-kubernetes-部署) | 大型项目、微服务 | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ |

## 🐳 Docker Compose（推荐）

### 一键部署完整环境

```bash
# 1. 下载项目
git clone https://gitee.com/zhijiantianya/ruoyi-vue-pro.git
cd ruoyi-vue-pro/script/docker

# 2. 一键启动（包含 MySQL + Redis + 应用 + 前端）
docker compose up -d

# 3. 查看状态
docker compose ps
```

### 服务访问地址

| 服务 | 地址 | 账号密码 |
|------|------|----------|
| 🌐 管理后台 | http://localhost:8080 | admin / admin123 |
| 📡 后端API | http://localhost:48080 | - |
| 🗄 MySQL | localhost:3306 | root / 123456 |
| 📦 Redis | localhost:6379 | - |

### 自定义配置

<details>
<summary>📝 <strong>修改环境变量</strong></summary>

```bash
# 编辑配置文件
vim docker.env

# 主要配置项
SERVER_PORT=48080           # 后端端口
MYSQL_ROOT_PASSWORD=123456  # MySQL密码
REDIS_PASSWORD=             # Redis密码（可选）
```

</details>

<details>
<summary>🔧 <strong>仅部署后端</strong></summary>

```yaml
# docker-compose-backend.yml
version: '3.8'
services:
  yudao-server:
    image: registry.cn-hangzhou.aliyuncs.com/yudao/yudao-server:latest
    ports:
      - "48080:48080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - DB_HOST=your-mysql-host
      - DB_USER=yudao
      - DB_PASSWORD=your-password
      - REDIS_HOST=your-redis-host
```

</details>

## 🖥 传统部署

### 环境准备

```bash
# 安装 JDK 11
curl -O https://download.oracle.com/java/17/latest/jdk-17_linux-x64_bin.tar.gz
tar -xzf jdk-17_linux-x64_bin.tar.gz
export JAVA_HOME=/path/to/jdk-17

# 安装 MySQL 8.0
wget https://dev.mysql.com/get/mysql80-community-release-el7-3.noarch.rpm
rpm -ivh mysql80-community-release-el7-3.noarch.rpm
yum install mysql-server

# 安装 Redis
yum install redis
```

### 快速部署脚本

<details>
<summary>📜 <strong>一键部署脚本</strong></summary>

```bash
#!/bin/bash
# deploy.sh - 一键部署脚本

set -e

APP_NAME="yudao-server"
APP_PORT=48080
DEPLOY_DIR="/opt/yudao"

echo "🚀 开始部署 $APP_NAME..."

# 1. 创建目录
mkdir -p $DEPLOY_DIR/{logs,config,backup}

# 2. 停止旧服务
if [ -f "$DEPLOY_DIR/app.pid" ]; then
    PID=$(cat $DEPLOY_DIR/app.pid)
    if ps -p $PID > /dev/null; then
        echo "停止旧服务 PID: $PID"
        kill $PID
        sleep 5
    fi
fi

# 3. 备份旧版本
if [ -f "$DEPLOY_DIR/$APP_NAME.jar" ]; then
    mv $DEPLOY_DIR/$APP_NAME.jar $DEPLOY_DIR/backup/$APP_NAME-$(date +%Y%m%d-%H%M%S).jar
fi

# 4. 部署新版本
cp target/$APP_NAME.jar $DEPLOY_DIR/
cp src/main/resources/application-prod.yaml $DEPLOY_DIR/config/

# 5. 启动服务
cd $DEPLOY_DIR
nohup java -jar \
    -Xms1g -Xmx2g \
    -XX:+UseG1GC \
    -Dspring.profiles.active=prod \
    -Dspring.config.additional-location=config/ \
    $APP_NAME.jar > logs/app.log 2>&1 &

echo $! > app.pid
echo "✅ 部署完成！PID: $(cat app.pid)"
echo "📊 查看日志: tail -f $DEPLOY_DIR/logs/app.log"
echo "🌐 访问地址: http://localhost:$APP_PORT"
```

</details>

### 系统服务配置

```bash
# 创建系统服务
sudo tee /etc/systemd/system/yudao.service > /dev/null <<EOF
[Unit]
Description=YuDao Application
After=network.target mysql.service redis.service

[Service]
Type=simple
User=root
WorkingDirectory=/opt/yudao
ExecStart=/usr/bin/java -jar -Xms1g -Xmx2g -XX:+UseG1GC yudao-server.jar
ExecStop=/bin/kill -SIGTERM \$MAINPID
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
EOF

# 启用并启动服务
sudo systemctl daemon-reload
sudo systemctl enable yudao
sudo systemctl start yudao
```

## ☸️ Kubernetes 部署

### Helm 一键部署（推荐）

```bash
# 1. 添加 Helm 仓库
helm repo add yudao https://charts.yudao.iocoder.cn
helm repo update

# 2. 创建命名空间
kubectl create namespace yudao

# 3. 部署应用
helm install yudao-server yudao/yudao-server \
  --namespace yudao \
  --set image.tag=latest \
  --set mysql.enabled=true \
  --set redis.enabled=true

# 4. 查看状态
kubectl get pods -n yudao
```

### 手动部署

<details>
<summary>⚙️ <strong>完整的 K8s 配置文件</strong></summary>

```yaml
# yudao-all.yaml
apiVersion: v1
kind: Namespace
metadata:
  name: yudao
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: yudao-server
  namespace: yudao
spec:
  replicas: 2
  selector:
    matchLabels:
      app: yudao-server
  template:
    metadata:
      labels:
        app: yudao-server
    spec:
      containers:
      - name: yudao-server
        image: registry.cn-hangzhou.aliyuncs.com/yudao/yudao-server:latest
        ports:
        - containerPort: 48080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "k8s"
        resources:
          requests:
            memory: 1Gi
            cpu: 500m
          limits:
            memory: 2Gi
            cpu: 1000m
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 48080
          initialDelaySeconds: 60
        readinessProbe:
          httpGet:
            path: /actuator/health
            port: 48080
          initialDelaySeconds: 30
---
apiVersion: v1
kind: Service
metadata:
  name: yudao-service
  namespace: yudao
spec:
  selector:
    app: yudao-server
  ports:
  - port: 48080
    targetPort: 48080
  type: LoadBalancer
```

</details>

## 🔧 生产环境优化

### JVM 参数调优

```bash
# 推荐的生产环境 JVM 参数
JAVA_OPTS="
-server
-Xms2g -Xmx4g
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
-XX:+UseStringDeduplication
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/opt/yudao/logs/
-Dspring.profiles.active=prod
"
```

### 数据库优化

```sql
-- MySQL 生产环境配置 (/etc/my.cnf)
[mysqld]
# 基础配置
port = 3306
datadir = /var/lib/mysql
socket = /var/lib/mysql/mysql.sock

# 性能配置
innodb_buffer_pool_size = 70%  # 服务器内存的70%
innodb_log_file_size = 256M
innodb_flush_log_at_trx_commit = 2
max_connections = 1000
query_cache_size = 64M

# 字符集
character-set-server = utf8mb4
collation-server = utf8mb4_general_ci
```

### Nginx 反向代理

```nginx
# /etc/nginx/conf.d/yudao.conf
upstream yudao_backend {
    server 127.0.0.1:48080 weight=1 max_fails=2 fail_timeout=30s;
    # server 127.0.0.1:48081 weight=1 max_fails=2 fail_timeout=30s;  # 多实例
}

server {
    listen 80;
    server_name your-domain.com;
    
    # 重定向到 HTTPS
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name your-domain.com;
    
    # SSL 配置
    ssl_certificate /path/to/certificate.crt;
    ssl_certificate_key /path/to/private.key;
    ssl_protocols TLSv1.2 TLSv1.3;
    
    # 后端API
    location /admin-api/ {
        proxy_pass http://yudao_backend;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # 超时配置
        proxy_connect_timeout 30s;
        proxy_send_timeout 30s;
        proxy_read_timeout 30s;
    }
    
    # 前端静态文件
    location / {
        root /var/www/yudao-ui;
        try_files $uri $uri/ /index.html;
        
        # 缓存配置
        location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg)$ {
            expires 1y;
            add_header Cache-Control "public, immutable";
        }
    }
}
```

## 📊 监控告警

### 应用监控

```yaml
# application-prod.yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: always
  metrics:
    export:
      prometheus:
        enabled: true

logging:
  level:
    root: INFO
  pattern:
    file: '%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n'
  file:
    name: /opt/yudao/logs/app.log
  logback:
    rollingpolicy:
      max-file-size: 100MB
      max-history: 30
```

### 健康检查脚本

```bash
#!/bin/bash
# health-check.sh

APP_URL="http://localhost:48080"
HEALTH_URL="$APP_URL/actuator/health"

# 检查应用健康状态
check_health() {
    local status=$(curl -s -o /dev/null -w "%{http_code}" $HEALTH_URL)
    if [ "$status" = "200" ]; then
        echo "✅ 应用健康状态正常"
        return 0
    else
        echo "❌ 应用健康检查失败，状态码: $status"
        return 1
    fi
}

# 检查端口
check_port() {
    if netstat -tuln | grep -q ":48080 "; then
        echo "✅ 端口 48080 正在监听"
        return 0
    else
        echo "❌ 端口 48080 未监听"
        return 1
    fi
}

# 主检查函数
main() {
    echo "🔍 开始健康检查..."
    
    if check_port && check_health; then
        echo "🎉 所有检查通过！"
        exit 0
    else
        echo "⚠️ 检查失败，请查看日志"
        exit 1
    fi
}

main
```

## 🔄 部署工具

### 自动化部署脚本

<details>
<summary>🤖 <strong>Jenkins Pipeline</strong></summary>

```groovy
pipeline {
    agent any
    
    stages {
        stage('拉取代码') {
            steps {
                git branch: 'master', url: 'https://gitee.com/zhijiantianya/ruoyi-vue-pro.git'
            }
        }
        
        stage('构建') {
            steps {
                sh 'mvn clean package -Dmaven.test.skip=true'
            }
        }
        
        stage('构建镜像') {
            steps {
                sh '''
                    docker build -t yudao-server:${BUILD_NUMBER} .
                    docker tag yudao-server:${BUILD_NUMBER} yudao-server:latest
                '''
            }
        }
        
        stage('部署') {
            steps {
                sh '''
                    docker stop yudao-server || true
                    docker rm yudao-server || true
                    docker run -d --name yudao-server \
                        -p 48080:48080 \
                        -v /opt/yudao/logs:/app/logs \
                        yudao-server:latest
                '''
            }
        }
    }
}
```

</details>

### 零停机部署

```bash
#!/bin/bash
# blue-green-deploy.sh - 蓝绿部署脚本

BLUE_PORT=48080
GREEN_PORT=48081
NGINX_UPSTREAM="yudao_backend"

# 检查当前活跃端口
get_active_port() {
    if curl -s http://localhost:$BLUE_PORT/actuator/health > /dev/null; then
        echo $BLUE_PORT
    else
        echo $GREEN_PORT
    fi
}

# 部署到非活跃端口
deploy() {
    local active_port=$(get_active_port)
    local deploy_port=$([[ $active_port == $BLUE_PORT ]] && echo $GREEN_PORT || echo $BLUE_PORT)
    
    echo "🚀 部署到端口: $deploy_port"
    
    # 停止旧服务
    docker stop yudao-server-$deploy_port || true
    
    # 启动新服务
    docker run -d --name yudao-server-$deploy_port \
        -p $deploy_port:48080 \
        yudao-server:latest
    
    # 等待服务启动
    echo "⏳ 等待服务启动..."
    for i in {1..30}; do
        if curl -s http://localhost:$deploy_port/actuator/health > /dev/null; then
            echo "✅ 服务启动成功"
            break
        fi
        sleep 2
    done
    
    # 切换流量
    echo "🔄 切换流量到端口: $deploy_port"
    # 这里需要更新 Nginx 配置或负载均衡器配置
    
    echo "🎉 部署完成！"
}

deploy
```

## ⚠️ 故障排查

### 常见问题速查

| 问题 | 可能原因 | 解决方案 |
|------|----------|----------|
| 启动失败 | JVM内存不足 | 调整 `-Xmx` 参数 |
| 连接超时 | 防火墙阻拦 | 开放端口：`firewall-cmd --add-port=48080/tcp` |
| 数据库连接失败 | 密码错误 | 检查配置文件中的数据库密码 |
| Redis连接失败 | 服务未启动 | `systemctl start redis` |
| 内存泄漏 | 代码问题 | 生成堆转储：`jmap -dump:format=b,file=heap.dump <pid>` |

### 日志分析命令

```bash
# 查看实时日志
tail -f /opt/yudao/logs/app.log

# 查看错误日志
grep -i error /opt/yudao/logs/app.log | tail -20

# 查看访问量统计
grep "GET\|POST" /var/log/nginx/access.log | wc -l

# 查看慢查询
grep "slow query" /var/log/mysql/mysql-slow.log
```

## 📚 相关资源

| 资源 | 链接 | 说明 |
|------|------|------|
| 🐳 Docker镜像 | [Docker Hub](https://hub.docker.com/r/yudao/yudao-server) | 官方镜像仓库 |
| ☸️ Helm Charts | [Helm仓库](https://charts.yudao.iocoder.cn) | K8s部署模板 |
| 📖 运维文档 | [官方文档](https://doc.iocoder.cn/deploy/) | 详细部署文档 |
| 💬 技术支持 | QQ群：3147719 | 部署问题咨询 |

---

**🎯 快速选择部署方式**

- 🚀 **快速体验** → Docker Compose 一键部署
- 🏢 **生产环境** → 传统部署 + Nginx + 监控
- 🌐 **大型项目** → Kubernetes + Helm + DevOps
- 🔧 **定制需求** → 手动配置 + 脚本自动化