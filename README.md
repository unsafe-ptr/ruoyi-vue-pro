# 🚀 芋道 ruoyi-vue-pro

<div align="center">

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-brightgreen.svg)
![Vue](https://img.shields.io/badge/Vue-3.2-brightgreen.svg)
![License](https://img.shields.io/github/license/YunaiV/ruoyi-vue-pro.svg)
![GitHub stars](https://img.shields.io/github/stars/YunaiV/ruoyi-vue-pro.svg?style=social)

**基于 Spring Boot + Vue 的企业级快速开发平台**

**🔥 100% 开源免费，个人与企业可免费使用**

[在线演示](http://dashboard-vue3.yudao.iocoder.cn) | [快速开始](https://doc.iocoder.cn/quick-start/) | [开发文档](https://doc.iocoder.cn/) | [视频教程](https://doc.iocoder.cn/video/)

</div>

## ✨ 特性

- 🎯 **开箱即用** - 基于最新技术栈，提供完整的前后端解决方案
- 🔐 **权限管理** - 完善的权限认证体系，支持多租户、多终端
- 🔄 **工作流程** - 集成 Flowable，支持可视化流程设计
- 🛒 **业务模块** - 内置 CRM、ERP、商城、会员等业务系统
- 🎨 **多端支持** - 支持 PC、移动端、小程序
- 🚀 **高性能** - Redis 缓存、MySQL 读写分离、分布式架构
- 📊 **数据报表** - 支持报表设计器、大屏设计器
- 🤖 **AI 集成** - 支持主流 AI 大模型接入

## 🎯 在线演示

| 版本 | 地址 | 账号 |
|------|------|------|
| Vue3 + Element Plus | [dashboard-vue3.yudao.iocoder.cn](http://dashboard-vue3.yudao.iocoder.cn) | admin/admin123 |
| Vue3 + Ant Design | [dashboard-vben.yudao.iocoder.cn](http://dashboard-vben.yudao.iocoder.cn) | admin/admin123 |
| Vue2 + Element UI | [dashboard.yudao.iocoder.cn](http://dashboard.yudao.iocoder.cn) | admin/admin123 |

## 🏗️ 项目结构

```
ruoyi-vue-pro/
├── yudao-dependencies/          # Maven 依赖管理
├── yudao-framework/            # 框架核心
├── yudao-server/              # 服务启动器
├── yudao-module-system/       # 系统管理模块
├── yudao-module-infra/        # 基础设施模块
├── yudao-module-bpm/          # 工作流程模块
├── yudao-module-pay/          # 支付系统模块
├── yudao-module-mall/         # 商城系统模块
├── yudao-module-crm/          # CRM 客户管理模块
├── yudao-module-erp/          # ERP 企业资源计划模块
├── yudao-module-ai/           # AI 大模型模块
├── yudao-module-member/       # 会员中心模块
├── yudao-module-mp/           # 微信公众号模块
├── yudao-module-report/       # 数据报表模块
└── yudao-ui/                  # 前端项目
```

## 🛠️ 技术栈

### 后端技术
- **核心框架**: Spring Boot 2.7.18
- **安全框架**: Spring Security + JWT
- **持久层**: MyBatis Plus + Druid
- **数据库**: MySQL 5.7/8.0+
- **缓存**: Redis + Redisson
- **工作流**: Flowable
- **任务调度**: Quartz
- **接口文档**: Swagger3 + Knife4j

### 前端技术
- **框架**: Vue 3.x / Vue 2.x
- **UI 组件**: Element Plus / Ant Design Vue / Element UI
- **构建工具**: Vite / Webpack
- **路由**: Vue Router
- **状态管理**: Pinia / Vuex
- **HTTP 客户端**: Axios

### 开发工具
- **IDE**: IntelliJ IDEA / VS Code
- **版本控制**: Git
- **项目管理**: Maven
- **代码规范**: ESLint + Prettier
- **API 测试**: Postman / ApiPost

## 🚀 快速开始

### 环境要求

- JDK 8 或 JDK 17/21
- MySQL 5.7+
- Redis 3.0+
- Maven 3.6+
- Node.js 16+

### 后端启动

1. **克隆项目**
```bash
git clone https://github.com/YunaiV/ruoyi-vue-pro.git
cd ruoyi-vue-pro
```

2. **创建数据库**
```bash
# 创建数据库
mysql -u root -p
CREATE DATABASE ruoyi_vue_pro DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入初始化脚本
source sql/mysql/ruoyi-vue-pro.sql
source sql/mysql/quartz.sql
```

3. **修改配置**
```yaml
# application-local.yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/ruoyi_vue_pro
    username: root
    password: 123456
  data:
    redis:
      host: 127.0.0.1
      port: 6379
      password: # Redis 密码，没有可不填
```

4. **启动项目**
```bash
mvn clean install
cd yudao-server
mvn spring-boot:run
```

### 前端启动

```bash
# 进入前端目录（以 Vue3 版本为例）
cd yudao-ui/yudao-ui-admin-vue3

# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 访问地址：http://localhost:80
```

## 📚 核心功能

### 🔐 系统管理
- **用户管理**: 用户信息维护、角色分配
- **角色管理**: 角色权限分配、数据权限设置
- **菜单管理**: 菜单配置、按钮权限控制
- **部门管理**: 组织架构管理、数据权限划分
- **租户管理**: SaaS 多租户支持
- **字典管理**: 系统字典维护
- **操作日志**: 系统操作记录追踪

### 📊 工作流程
- **流程设计**: 可视化流程设计器
- **表单设计**: 动态表单构建器
- **流程实例**: 流程发起、审批、监控
- **任务处理**: 待办事项、已办查询
- **流程监控**: 流程实例监控、统计分析

### 💰 支付系统
- **支付渠道**: 支付宝、微信支付接入
- **支付订单**: 支付订单管理、退款处理
- **商户管理**: 商户信息、应用配置
- **对账管理**: 交易对账、财务报表

### 🛍️ 商城系统
- **商品管理**: 商品信息、分类、品牌管理
- **订单管理**: 订单处理、物流跟踪
- **促销活动**: 优惠券、拼团、秒杀
- **会员系统**: 会员等级、积分、成长值

### 🏢 CRM 系统
- **客户管理**: 客户信息、跟进记录
- **商机管理**: 销售机会、转化追踪
- **合同管理**: 合同签署、执行监控
- **回款管理**: 回款计划、到账记录

### 📦 ERP 系统
- **采购管理**: 采购订单、供应商管理
- **销售管理**: 销售订单、客户管理
- **库存管理**: 出入库、库存盘点
- **财务管理**: 应收应付、财务报表

## 🌟 开源协议

本项目基于 [MIT License](./LICENSE) 开源协议，您可以：

✅ **商业使用** - 可用于商业项目  
✅ **自由修改** - 可自由修改代码  
✅ **自由分发** - 可自由分享给他人  
✅ **私用** - 可用于个人项目  

## 🤝 参与贡献

欢迎各种形式的贡献，包括但不限于：

- 🐛 报告 Bug
- 💡 提出新功能建议  
- 📝 完善文档
- 🔧 提交代码

### 贡献流程

1. Fork 本项目
2. 创建特性分支: `git checkout -b feature/AmazingFeature`
3. 提交更改: `git commit -m 'Add some AmazingFeature'`
4. 推送到分支: `git push origin feature/AmazingFeature`
5. 提交 Pull Request

## 📞 联系我们

- 📧 **邮箱**: yunai@iocoder.cn
- 💬 **微信**: Aix9975（项目外包合作）
- 🌐 **官网**: [https://www.iocoder.cn](https://www.iocoder.cn)
- 📖 **文档**: [https://doc.iocoder.cn](https://doc.iocoder.cn)

## ⭐ Star History

如果这个项目对您有帮助，请给我们一个 ⭐️ Star！您的支持是我们前进的动力。

[![Star History Chart](https://api.star-history.com/svg?repos=YunaiV/ruoyi-vue-pro&type=Date)](https://star-history.com/#YunaiV/ruoyi-vue-pro&Date)

## 📄 许可证

本项目采用 MIT 许可证。详情请参见 [LICENSE](./LICENSE) 文件。

---

<div align="center">

**让开发更简单，让创业更轻松** 🚀

Made with ❤️ by [芋道源码](https://www.iocoder.cn)

</div>
