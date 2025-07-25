# 🚀 部署指南

本文档提供芋道项目在不同环境下的部署方案，包括传统部署、Docker 部署、Kubernetes 部署等。

## 📋 部署前准备

### 环境要求
- **操作系统**：Linux (CentOS 7+, Ubuntu 18+) / Windows Server / macOS
- **JDK**：1.8+ 或 11+（推荐 OpenJDK 11）
- **数据库**：MySQL 5.7+ / 8.0+
- **缓存**：Redis 5.0+
- **内存**：最少 2GB，推荐 4GB+
- **存储**：最少 10GB 可用空间

### 构建准备
```bash
# 1. 确保 Maven 环境
mvn -version

# 2. 清理并构建项目
mvn clean package -Dmaven.test.skip=true

# 3. 检查构建产物
ls -la yudao-server/target/yudao-server.jar
```

## 🖥 传统部署（Linux）

### 1. 服务器准备
```bash
# 创建应用目录
mkdir -p /opt/yudao
cd /opt/yudao

# 创建必要的目录
mkdir -p logs config data
```

### 2. 安装 JDK
```bash
# CentOS/RHEL
yum install -y java-11-openjdk java-11-openjdk-devel

# Ubuntu/Debian
apt update
apt install -y openjdk-11-jdk

# 验证安装
java -version
```

### 3. 安装 MySQL
```bash
# CentOS 8
dnf install -y mysql-server
systemctl start mysqld
systemctl enable mysqld

# 安全设置
mysql_secure_installation

# 创建数据库
mysql -u root -p
CREATE DATABASE ruoyi_vue_pro DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
CREATE USER 'yudao'@'%' IDENTIFIED BY 'YourStrongPassword';
GRANT ALL PRIVILEGES ON ruoyi_vue_pro.* TO 'yudao'@'%';
FLUSH PRIVILEGES;
```

### 4. 安装 Redis
```bash
# CentOS/RHEL
yum install -y epel-release
yum install -y redis

# Ubuntu/Debian
apt install -y redis-server

# 启动服务
systemctl start redis
systemctl enable redis

# 配置密码（可选）
echo "requirepass YourRedisPassword" >> /etc/redis/redis.conf
systemctl restart redis
```

### 5. 数据库初始化
```bash
# 上传 SQL 文件到服务器
scp sql/mysql/*.sql user@server:/opt/yudao/

# 导入数据
mysql -u yudao -p ruoyi_vue_pro < /opt/yudao/ruoyi-vue-pro.sql
mysql -u yudao -p ruoyi_vue_pro < /opt/yudao/quartz.sql
```

### 6. 应用配置
```bash
# 创建生产环境配置
cat > /opt/yudao/config/application-prod.yaml << EOF
spring:
  datasource:
    dynamic:
      primary: master
      datasource:
        master:
          url: jdbc:mysql://localhost:3306/ruoyi_vue_pro?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&nullCatalogMeansCurrent=true
          username: yudao
          password: YourStrongPassword
          
  redis:
    host: localhost
    port: 6379
    password: YourRedisPassword
    database: 1

server:
  port: 48080
  
logging:
  level:
    root: INFO
  file:
    path: /opt/yudao/logs
EOF
```

### 7. 部署应用
```bash
# 上传 JAR 文件
scp yudao-server/target/yudao-server.jar user@server:/opt/yudao/

# 创建启动脚本
cat > /opt/yudao/start.sh << EOF
#!/bin/bash
export JAVA_OPTS="-Xms1g -Xmx2g -XX:+UseG1GC"
export APP_OPTS="--spring.profiles.active=prod --spring.config.additional-location=/opt/yudao/config/"

cd /opt/yudao
nohup java \$JAVA_OPTS -jar yudao-server.jar \$APP_OPTS > logs/app.log 2>&1 &
echo \$! > app.pid
EOF

chmod +x /opt/yudao/start.sh

# 创建停止脚本
cat > /opt/yudao/stop.sh << EOF
#!/bin/bash
cd /opt/yudao
if [ -f app.pid ]; then
    kill \$(cat app.pid)
    rm -f app.pid
    echo "Application stopped"
else
    echo "Application is not running"
fi
EOF

chmod +x /opt/yudao/stop.sh
```

### 8. 启动应用
```bash
cd /opt/yudao
./start.sh

# 检查启动状态
tail -f logs/app.log

# 验证服务
curl http://localhost:48080/admin-api/system/auth/get-permission-info
```

### 9. 系统服务配置
```bash
# 创建 systemd 服务文件
cat > /etc/systemd/system/yudao.service << EOF
[Unit]
Description=YuDao Application
After=network.target

[Service]
Type=forking
ExecStart=/opt/yudao/start.sh
ExecStop=/opt/yudao/stop.sh
User=root
Group=root
WorkingDirectory=/opt/yudao

[Install]
WantedBy=multi-user.target
EOF

# 启用服务
systemctl daemon-reload
systemctl enable yudao
systemctl start yudao
systemctl status yudao
```

## 🐳 Docker 部署

### 1. 单容器部署

#### 构建镜像
```bash
# 使用项目提供的 Dockerfile
cd yudao-server
docker build -t yudao-server:latest .

# 或使用 Maven 插件构建
mvn clean package dockerfile:build
```

#### 运行容器
```bash
docker run -d \
  --name yudao-server \
  -p 48080:48080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_HOST=your-mysql-host \
  -e DB_USER=yudao \
  -e DB_PASSWORD=YourPassword \
  -e REDIS_HOST=your-redis-host \
  -e REDIS_PASSWORD=YourRedisPassword \
  -v /opt/yudao/logs:/app/logs \
  yudao-server:latest
```

### 2. Docker Compose 部署

#### 完整服务栈
```yaml
# docker-compose.yml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: yudao-mysql
    environment:
      MYSQL_ROOT_PASSWORD: root123
      MYSQL_DATABASE: ruoyi_vue_pro
      MYSQL_USER: yudao
      MYSQL_PASSWORD: yudao123
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
      - ./sql/mysql:/docker-entrypoint-initdb.d
    command: --default-authentication-plugin=mysql_native_password
    
  redis:
    image: redis:6.2-alpine
    container_name: yudao-redis
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data
    command: redis-server --appendonly yes --requirepass redis123
    
  yudao-server:
    build: ./yudao-server
    container_name: yudao-server
    ports:
      - "48080:48080"
    environment:
      SPRING_PROFILES_ACTIVE: docker
      DB_HOST: mysql
      DB_USER: yudao
      DB_PASSWORD: yudao123
      REDIS_HOST: redis
      REDIS_PASSWORD: redis123
    depends_on:
      - mysql
      - redis
    volumes:
      - ./logs:/app/logs
      
  nginx:
    image: nginx:alpine
    container_name: yudao-nginx
    ports:
      - "80:80"
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf
      - ./dist:/usr/share/nginx/html
    depends_on:
      - yudao-server

volumes:
  mysql_data:
  redis_data:
```

#### 启动服务
```bash
# 使用项目提供的配置
cd script/docker
docker compose --env-file docker.env up -d

# 查看服务状态
docker compose ps

# 查看日志
docker compose logs -f yudao-server
```

## ☸️ Kubernetes 部署

### 1. 创建命名空间
```yaml
# namespace.yaml
apiVersion: v1
kind: Namespace
metadata:
  name: yudao
```

### 2. ConfigMap 配置
```yaml
# configmap.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: yudao-config
  namespace: yudao
data:
  application-k8s.yaml: |
    spring:
      datasource:
        dynamic:
          primary: master
          datasource:
            master:
              url: jdbc:mysql://mysql-service:3306/ruoyi_vue_pro?useSSL=false&serverTimezone=Asia/Shanghai
              username: yudao
              password: yudao123
      redis:
        host: redis-service
        port: 6379
        password: redis123
        database: 1
```

### 3. MySQL 部署
```yaml
# mysql-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mysql
  namespace: yudao
spec:
  replicas: 1
  selector:
    matchLabels:
      app: mysql
  template:
    metadata:
      labels:
        app: mysql
    spec:
      containers:
      - name: mysql
        image: mysql:8.0
        ports:
        - containerPort: 3306
        env:
        - name: MYSQL_ROOT_PASSWORD
          value: "root123"
        - name: MYSQL_DATABASE
          value: "ruoyi_vue_pro"
        - name: MYSQL_USER
          value: "yudao"
        - name: MYSQL_PASSWORD
          value: "yudao123"
        volumeMounts:
        - name: mysql-storage
          mountPath: /var/lib/mysql
      volumes:
      - name: mysql-storage
        persistentVolumeClaim:
          claimName: mysql-pvc
---
apiVersion: v1
kind: Service
metadata:
  name: mysql-service
  namespace: yudao
spec:
  selector:
    app: mysql
  ports:
  - port: 3306
    targetPort: 3306
```

### 4. 应用部署
```yaml
# yudao-deployment.yaml
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
        image: yudao-server:latest
        ports:
        - containerPort: 48080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "k8s"
        - name: SPRING_CONFIG_ADDITIONAL_LOCATION
          value: "/config/"
        volumeMounts:
        - name: config-volume
          mountPath: /config
        resources:
          requests:
            memory: "1Gi"
            cpu: "500m"
          limits:
            memory: "2Gi"
            cpu: "1000m"
        livenessProbe:
          httpGet:
            path: /admin-api/system/auth/get-permission-info
            port: 48080
          initialDelaySeconds: 60
          periodSeconds: 30
        readinessProbe:
          httpGet:
            path: /admin-api/system/auth/get-permission-info
            port: 48080
          initialDelaySeconds: 30
          periodSeconds: 10
      volumes:
      - name: config-volume
        configMap:
          name: yudao-config
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

### 5. Ingress 配置
```yaml
# ingress.yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: yudao-ingress
  namespace: yudao
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /
spec:
  rules:
  - host: yudao.yourdomain.com
    http:
      paths:
      - path: /admin-api
        pathType: Prefix
        backend:
          service:
            name: yudao-service
            port:
              number: 48080
```

### 6. 部署命令
```bash
# 应用配置
kubectl apply -f namespace.yaml
kubectl apply -f configmap.yaml
kubectl apply -f mysql-deployment.yaml
kubectl apply -f yudao-deployment.yaml
kubectl apply -f ingress.yaml

# 查看部署状态
kubectl get pods -n yudao
kubectl get services -n yudao
kubectl get ingress -n yudao

# 查看日志
kubectl logs -f deployment/yudao-server -n yudao
```

## 🔧 生产环境优化

### 1. JVM 调优
```bash
export JAVA_OPTS="
-Xms2g -Xmx4g
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
-XX:+UseStringDeduplication
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/opt/yudao/logs/heapdump.hprof
-XX:+PrintGCDetails
-XX:+PrintGCTimeStamps
-Xloggc:/opt/yudao/logs/gc.log
"
```

### 2. 数据库优化
```sql
-- MySQL 配置优化 (/etc/my.cnf)
[mysqld]
innodb_buffer_pool_size = 2G
innodb_log_file_size = 256M
innodb_flush_log_at_trx_commit = 2
query_cache_size = 64M
max_connections = 1000
```

### 3. Redis 优化
```bash
# Redis 配置 (/etc/redis/redis.conf)
maxmemory 1gb
maxmemory-policy allkeys-lru
save 900 1
save 300 10
save 60 10000
```

### 4. Nginx 配置
```nginx
upstream yudao_backend {
    server 127.0.0.1:48080;
    # 如果有多个实例
    # server 127.0.0.1:48081;
}

server {
    listen 80;
    server_name yourdomain.com;
    
    location /admin-api/ {
        proxy_pass http://yudao_backend;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # 超时设置
        proxy_connect_timeout 30s;
        proxy_send_timeout 30s;
        proxy_read_timeout 30s;
    }
    
    location / {
        root /var/www/yudao-ui;
        try_files $uri $uri/ /index.html;
    }
}
```

## 📊 监控与日志

### 1. 应用监控
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
```

### 2. 日志配置
```xml
<!-- logback-spring.xml -->
<configuration>
    <springProfile name="prod">
        <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
            <file>/opt/yudao/logs/app.log</file>
            <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
                <fileNamePattern>/opt/yudao/logs/app.%d{yyyy-MM-dd}.%i.log.gz</fileNamePattern>
                <maxFileSize>100MB</maxFileSize>
                <maxHistory>30</maxHistory>
                <totalSizeCap>1GB</totalSizeCap>
            </rollingPolicy>
        </appender>
        
        <root level="INFO">
            <appender-ref ref="FILE"/>
        </root>
    </springProfile>
</configuration>
```

## 🚨 故障排查

### 常见问题
1. **内存不足**：调整 JVM 堆内存大小
2. **连接超时**：检查网络和防火墙设置
3. **数据库连接池耗尽**：调整连接池配置
4. **Redis 连接失败**：检查 Redis 服务状态和密码

### 健康检查脚本
```bash
#!/bin/bash
# health-check.sh

APP_URL="http://localhost:48080/admin-api/system/auth/get-permission-info"
HEALTH_URL="http://localhost:48080/actuator/health"

# 检查应用状态
if curl -f -s $HEALTH_URL > /dev/null; then
    echo "✅ Application is healthy"
else
    echo "❌ Application is not responding"
    exit 1
fi

# 检查数据库连接
if curl -f -s $APP_URL > /dev/null; then
    echo "✅ Database connection is OK"
else
    echo "❌ Database connection failed"
    exit 1
fi
```

## 📚 相关资源

- [官方部署文档](https://doc.iocoder.cn/quick-start/)
- [Docker 部署详解](./script/docker/Docker-HOWTO.md)
- [性能调优指南](https://doc.iocoder.cn/performance/)
- [监控告警配置](https://doc.iocoder.cn/monitor/)