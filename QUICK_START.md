# 🚀 快速开始指南

> **3分钟搭建完整的企业级开发平台！**

<div align="center">

[![JDK](https://img.shields.io/badge/JDK-8+-green.svg)](https://www.oracle.com/java/technologies/javase-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-blue.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-5.7+-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-5.0+-red.svg)](https://redis.io/)

</div>

## ⚡ 一键启动（推荐）

### 🐳 Docker 方式（最简单）

```bash
# 1. 克隆项目
git clone https://gitee.com/zhijiantianya/ruoyi-vue-pro.git
cd ruoyi-vue-pro

# 2. 一键启动（包含前端+后端+数据库+Redis）
cd script/docker
docker compose up -d

# 3. 访问系统
# 前端地址：http://localhost:8080
# 后端地址：http://localhost:48080
# 默认账号：admin / admin123
```

✅ **就这么简单！** 系统已经可以使用了。

## 💻 传统方式启动

| 软件 | 版本要求 | 说明 |
|------|----------|------|
| JDK | 8+ | 推荐 JDK 11 |
| Maven | 3.6+ | 项目构建工具 |
| MySQL | 5.7+ | 主数据库 |
| Redis | 5.0+ | 缓存数据库 |

### 快速启动
```bash
# 1. 克隆项目
git clone https://gitee.com/zhijiantianya/ruoyi-vue-pro.git
cd ruoyi-vue-pro

# 2. 初始化数据库
mysql -u root -p -e "CREATE DATABASE ruoyi_vue_pro DEFAULT CHARACTER SET utf8mb4;"
mysql -u root -p ruoyi_vue_pro < sql/mysql/ruoyi-vue-pro.sql

# 3. 修改数据库配置（编辑 yudao-server/src/main/resources/application-local.yaml）
# 4. 启动项目
mvn clean install && cd yudao-server && mvn spring-boot:run
```

| 服务 | 地址 | 说明 |
|------|------|------|
| 🌐 后端API | http://localhost:48080 | 接口服务 |
| 📚 接口文档 | http://localhost:48080/doc.html | Swagger文档 |
| 💻 管理后台 | 需启动前端项目 | Vue3管理界面 |

| 账号类型 | 用户名 | 密码 | 说明 |
|----------|--------|------|------|
| 超级管理员 | `admin` | `admin123` | 全部权限 |
| 普通用户 | `test` | `test123` | 部分权限 |

## 🌟 核心特性速览

<details>
<summary>🎛️ <strong>系统管理</strong> - 完整的RBAC权限体系</summary>

- 用户管理、角色管理、菜单管理
- 部门管理、岗位管理、字典管理
- 多租户SaaS、数据权限控制
- 操作日志、登录日志、错误码管理

</details>

<details>
<summary>🔄 <strong>工作流程</strong> - 仿钉钉+BPMN双引擎</summary>

- 在线流程设计器（仿钉钉风格）
- 专业BPMN流程设计器
- 会签、或签、依次审批、驳回、转办
- 动态表单、流程监控

</details>

<details>
<summary>🏪 <strong>商城系统</strong> - 完整电商解决方案</summary>

- 商品管理、订单管理、库存管理
- 优惠券、满减、秒杀、拼团
- 分销系统、积分系统、会员等级
- 支付对接（微信、支付宝）

</details>

<details>
<summary>🤖 <strong>AI大模型</strong> - 集成主流AI平台</summary>

- 对话聊天、图像生成、文档解析
- 支持OpenAI、文心一言、通义千问
- 知识库问答、AI写作助手

</details>

## 📱 启动前端（可选）

| 前端版本 | 特点 | 启动命令 |
|----------|------|----------|
| Vue3 + Element Plus | 🔥 推荐，功能最全 | [点击查看](https://gitee.com/yudaocode/yudao-ui-admin-vue3) |
| Vue3 + Ant Design | 💼 企业级设计 | [点击查看](https://gitee.com/yudaocode/yudao-ui-admin-vben) |
| Vue2 + Element UI | 📱 稳定版本 | [点击查看](https://gitee.com/yudaocode/yudao-ui-admin-vue2) |

## 🔧 实用工具

### 一键重置脚本
```bash
# 重置数据库（谨慎使用）
mysql -u root -p -e "DROP DATABASE IF EXISTS ruoyi_vue_pro; CREATE DATABASE ruoyi_vue_pro DEFAULT CHARACTER SET utf8mb4;"
mysql -u root -p ruoyi_vue_pro < sql/mysql/ruoyi-vue-pro.sql
```

### 健康检查脚本
```bash
# 检查服务状态
curl -s http://localhost:48080/actuator/health | jq .
```

### 常用端口检查
```bash
# 检查端口占用
netstat -tulpn | grep -E ':48080|:3306|:6379'
```

## ⚠️ 常见问题

<details>
<summary>❌ <strong>启动失败</strong></summary>

1. **端口被占用** → `lsof -i :48080` 查看占用进程
2. **JDK版本不对** → `java -version` 确认版本 ≥ 8
3. **Maven依赖** → `mvn clean install -U` 重新下载

</details>

<details>
<summary>🔌 <strong>数据库连接失败</strong></summary>

1. **服务未启动** → `systemctl status mysql`
2. **密码错误** → 检查 `application-local.yaml` 配置
3. **数据库不存在** → 重新执行初始化SQL

</details>

<details>
<summary>📡 <strong>Redis连接失败</strong></summary>

1. **服务未启动** → `systemctl status redis`
2. **密码配置** → 检查Redis密码设置
3. **端口访问** → `telnet localhost 6379`

</details>

## 🚀 下一步

| 操作 | 链接 | 说明 |
|------|------|------|
| 📖 深入学习 | [开发指南](./DEVELOPMENT.md) | 开发规范和最佳实践 |
| 🚀 生产部署 | [部署指南](./DEPLOYMENT.md) | 生产环境部署方案 |
| 🎮 在线体验 | [演示地址](http://dashboard-vue3.yudao.iocoder.cn) | 在线演示系统 |
| 💬 获取帮助 | [QQ群：3147719](https://doc.iocoder.cn/) | 技术交流群 |