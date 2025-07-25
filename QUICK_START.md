# 🚀 快速开始指南

本指南将帮助您在最短时间内搭建和运行芋道项目。

## 📋 前置要求

### 必需软件
- **JDK 8+** - Java 开发环境
- **Maven 3.6+** - 项目构建工具
- **MySQL 5.7/8.0+** - 主数据库
- **Redis 5.0+** - 缓存数据库
- **Node.js 14+** - 前端开发环境（如需运行前端）

### 推荐 IDE
- **IntelliJ IDEA** - 推荐使用，已配置相关插件
- **Eclipse** - 需要安装 Lombok 插件

## ⚡ 5分钟快速启动

### 1. 克隆项目
```bash
git clone https://gitee.com/zhijiantianya/ruoyi-vue-pro.git
cd ruoyi-vue-pro
```

### 2. 数据库准备
```bash
# 创建数据库
mysql -u root -p
CREATE DATABASE ruoyi_vue_pro DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

# 导入SQL文件
mysql -u root -p ruoyi_vue_pro < sql/mysql/ruoyi-vue-pro.sql
mysql -u root -p ruoyi_vue_pro < sql/mysql/quartz.sql
```

### 3. 修改配置
编辑 `yudao-server/src/main/resources/application-local.yaml`：
```yaml
spring:
  # 数据库连接
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/ruoyi_vue_pro?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&nullCatalogMeansCurrent=true
    username: root
    password: 你的密码
    
  # Redis 连接
  redis:
    host: 127.0.0.1
    port: 6379
    password: # 如果有密码请填写
```

### 4. 启动后端服务
```bash
# 方式一：使用 Maven
mvn clean install
cd yudao-server
mvn spring-boot:run

# 方式二：使用 IDE
# 直接运行 YudaoServerApplication.java 的 main 方法
```

### 5. 验证启动
- 浏览器访问：http://localhost:48080
- API 文档：http://localhost:48080/doc.html
- 管理后台：需要单独启动前端项目

## 🐳 Docker 快速启动

如果您希望使用 Docker 快速体验：

```bash
# 1. 构建项目
docker volume create --name yudao-maven-repo
docker run -it --rm --name yudao-maven \
    -v yudao-maven-repo:/root/.m2 \
    -v $PWD:/usr/src/mymaven \
    -w /usr/src/mymaven \
    maven mvn clean install package '-Dmaven.test.skip=true'

# 2. 启动服务
cd script/docker
docker compose --env-file docker.env up -d
```

访问地址：
- 管理后台：http://localhost:8080
- 后端API：http://localhost:48080
- 数据库：localhost:3306 (root/123456)

## 🎯 默认账号

| 角色 | 账号 | 密码 |
|-----|------|------|
| 超级管理员 | admin | admin123 |
| 普通用户 | test | test123 |

## 📱 前端项目

后端启动成功后，您可以选择启动前端项目：

### Vue3 + Element Plus 版本
```bash
git clone https://gitee.com/yudaocode/yudao-ui-admin-vue3.git
cd yudao-ui-admin-vue3
npm install
npm run dev
```

### Vue3 + Ant Design 版本
```bash
git clone https://gitee.com/yudaocode/yudao-ui-admin-vben.git
cd yudao-ui-admin-vben
npm install
npm run dev
```

## 🔧 常见问题

### 1. 启动失败
- **检查端口占用**：确保 48080 端口未被占用
- **检查数据库连接**：确认 MySQL 和 Redis 服务已启动
- **检查 JDK 版本**：确保使用 JDK 8 或更高版本

### 2. 数据库连接失败
- 确认数据库用户名密码正确
- 确认数据库已创建并导入SQL文件
- 检查数据库 URL 中的时区和字符集设置

### 3. Redis 连接失败
- 确认 Redis 服务已启动
- 检查 Redis 配置中的主机地址和端口
- 如果设置了密码，确保配置文件中密码正确

### 4. 前端代理失败
- 确保后端服务已成功启动
- 检查前端项目中的代理配置是否指向正确的后端地址

## 📚 下一步

启动成功后，您可以：
1. 浏览 [在线文档](https://doc.iocoder.cn/) 了解更多功能
2. 查看 [开发指南](./DEVELOPMENT.md) 了解开发规范
3. 访问 [演示地址](http://dashboard-vue3.yudao.iocoder.cn) 体验完整功能

## 🆘 获取帮助

- 📖 [官方文档](https://doc.iocoder.cn/)
- 💬 QQ群：3147719
- 🐛 [提交Issue](https://gitee.com/zhijiantianya/ruoyi-vue-pro/issues)
- 📺 [视频教程](https://doc.iocoder.cn/video/)