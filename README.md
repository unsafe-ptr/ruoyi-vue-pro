<div align="center">

# 🌟 芋道 ruoyi-vue-pro

**中国第一流的快速开发平台 | 全栈开源 | 企业级解决方案**

<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-2.7.18-brightgreen.svg" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Vue-3.2-brightgreen.svg" alt="Vue">
  <img src="https://img.shields.io/badge/JDK-1.8+-orange.svg" alt="JDK">
  <img src="https://img.shields.io/github/license/YunaiV/ruoyi-vue-pro" alt="License">
  <img src="https://img.shields.io/github/stars/YunaiV/ruoyi-vue-pro?style=social" alt="GitHub stars">
</p>

**🎯 严肃声明：现在、未来都不会有商业版本，所有代码全部开源！**

---

*「我喜欢写代码，乐此不疲」*  
*「我喜欢做开源，以此为乐」*

我 🐶 在上海艰苦奋斗，早中晚在 top3 大厂认真搬砖，夜里为开源做贡献。

如果这个项目让你有所收获，记得 ⭐ Star 关注哦，这对我是非常不错的鼓励与支持。

</div>

## 📋 目录

- [🚀 快速开始](#-快速开始)
- [🎯 项目特色](#-项目特色)
- [🎨 在线演示](#-在线演示)
- [📦 版本说明](#-版本说明)
- [🏗️ 架构设计](#-架构设计)
- [🛠️ 技术栈](#-技术栈)
- [📁 项目结构](#-项目结构)
- [💡 功能模块](#-功能模块)
- [🖥️ 演示图片](#-演示图片)
- [🤝 项目关系](#-项目关系)
- [📜 开源协议](#-开源协议)
- [💼 商务合作](#-商务合作)
- [🙏 致谢](#-致谢)

## 🚀 快速开始

### 📖 新手必读

| 资源类型 | 链接 | 说明 |
|---------|------|------|
| 🎥 **视频教程** | [https://doc.iocoder.cn/video/](https://doc.iocoder.cn/video/) | 从零开始的完整教程 |
| 📚 **启动文档** | [https://doc.iocoder.cn/quick-start/](https://doc.iocoder.cn/quick-start/) | 5分钟快速启动指南 |
| 🌐 **在线体验** | [dashboard-vue3.yudao.iocoder.cn](http://dashboard-vue3.yudao.iocoder.cn) | Vue3 + Element Plus |
| 🎯 **Vben版本** | [dashboard-vben.yudao.iocoder.cn](http://dashboard-vben.yudao.iocoder.cn) | Vue3 + Ant Design |

### ⚡ 一键启动

```bash
# 1. 克隆项目
git clone https://gitee.com/zhijiantianya/ruoyi-vue-pro.git

# 2. 导入数据库
# 执行 sql/mysql/ruoyi-vue-pro.sql 文件

# 3. 启动后端服务
cd yudao-server
mvn spring-boot:run

# 4. 启动前端项目（另开终端）
# 详见前端项目 README
```

## 🎯 项目特色

<div align="center">

### 🏆 为什么选择芋道？

</div>

| 特色 | 芋道优势 | 竞品对比 |
|------|----------|----------|
| 📖 **完全开源** | 100% 开源，MIT 协议 | 其他项目仅开源部分代码 |
| 🎯 **代码质量** | 11万行代码 + 4万行注释 | 代码规范，注释详细 |
| 🚀 **快速开发** | 一键生成 CRUD + 接口文档 | 提升开发效率 80% |
| 🌟 **功能丰富** | 涵盖 ERP、CRM、商城等 | 满足各种业务场景 |
| 🔧 **技术先进** | SpringBoot 2.7 + Vue3 | 主流技术栈 |
| 📱 **多端适配** | PC + 移动端 + 小程序 | 一套代码多终端 |

## 🎨 在线演示

<div align="center">

| 演示环境 | 链接 | 技术栈 |
|---------|------|--------|
| 🎪 **Vue3 主版本** | [dashboard-vue3.yudao.iocoder.cn](http://dashboard-vue3.yudao.iocoder.cn) | Vue3 + Element Plus |
| 🎭 **Vben 版本** | [dashboard-vben.yudao.iocoder.cn](http://dashboard-vben.yudao.iocoder.cn) | Vue3 + Ant Design |
| 🎨 **Vue2 版本** | [dashboard.yudao.iocoder.cn](http://dashboard.yudao.iocoder.cn) | Vue2 + Element UI |

**默认账号**：`admin` / `admin123`

</div>

## 📦 版本说明

<div align="center">

### 🌈 多版本支持，满足不同需求

</div>

| 版本类型 | JDK 8 + Spring Boot 2.7 | JDK 17/21 + Spring Boot 3.2 |
|---------|--------------------------|------------------------------|
| 🎯 **完整版** | [`master`](https://gitee.com/zhijiantianya/ruoyi-vue-pro/tree/master/) | [`master-jdk17`](https://gitee.com/zhijiantianya/ruoyi-vue-pro/tree/master-jdk17/) |
| ⚡ **精简版** | [`yudao-boot-mini`](https://gitee.com/yudaocode/yudao-boot-mini/tree/master/) | [`yudao-boot-mini`](https://gitee.com/yudaocode/yudao-boot-mini/tree/master-jdk17/) |

<details>
<summary>📋 版本功能对比</summary>

| 功能模块 | 完整版 | 精简版 |
|---------|--------|--------|
| 🔐 系统功能 | ✅ | ✅ |
| 🏗️ 基础设施 | ✅ | ✅ |
| 👥 会员中心 | ✅ | ❌ |
| 📊 数据报表 | ✅ | ❌ |
| 🔄 工作流程 | ✅ | ❌ |
| 🛒 商城系统 | ✅ | ❌ |
| 📱 微信公众号 | ✅ | ❌ |
| 👔 CRM 系统 | ✅ | ❌ |
| 🏭 ERP 系统 | ✅ | ❌ |
| 🤖 AI 大模型 | ✅ | ❌ |

</details>

## 🏗️ 架构设计

<div align="center">

![架构图](/.image/common/ruoyi-vue-pro-architecture.png)

### 🎯 技术选型说明

</div>

- **🎯 后端架构**：Spring Boot 多模块，模块化设计，易扩展
- **🎨 前端技术**：Vue3 + Element Plus / Ant Design，现代化UI
- **📱 移动端**：uni-app 一码多端，支持小程序、H5、APP
- **🗄️ 数据存储**：MySQL + MyBatis Plus + Redis，高性能组合
- **🔐 权限认证**：Spring Security + JWT + Redis，安全可靠
- **🌊 工作流**：Flowable，支持复杂业务流程
- **☁️ 微服务**：Spring Cloud Alibaba（可选），支持分布式部署

## 🛠️ 技术栈

<div align="center">

### 📚 核心框架

</div>

| 技术 | 版本 | 说明 | 官网 |
|------|------|------|------|
| Spring Boot | 2.7.18 | 核心框架 | [spring.io](https://spring.io/projects/spring-boot) |
| Spring Security | 5.7.11 | 安全框架 | [spring.io](https://spring.io/projects/spring-security) |
| MyBatis Plus | 3.5.7 | ORM框架 | [baomidou.com](https://mp.baomidou.com/) |
| Flowable | 6.8.0 | 工作流引擎 | [flowable.org](https://www.flowable.org/) |
| Redis | 5.0+ | 缓存数据库 | [redis.io](https://redis.io/) |
| MySQL | 5.7+ | 主数据库 | [mysql.com](https://www.mysql.com/) |

<details>
<summary>🔧 查看完整技术栈</summary>

### 🎨 前端技术

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.2 | 渐进式框架 |
| Element Plus | 最新 | 组件库 |
| TypeScript | 4.x | 静态类型 |
| Vite | 最新 | 构建工具 |

### 🛠️ 开发工具

| 技术 | 版本 | 说明 |
|------|------|------|
| Lombok | 1.18.34 | 简化代码 |
| MapStruct | 1.6.3 | Bean转换 |
| Swagger | 3.x | API文档 |
| Druid | 1.2.23 | 连接池 |

</details>

## 📁 项目结构

```
yudao/
├── 📂 yudao-dependencies/     # Maven 依赖管理
├── 📂 yudao-framework/        # 框架封装
├── 📂 yudao-server/          # 服务启动器
├── 📂 yudao-module-system/   # 系统管理模块
├── 📂 yudao-module-infra/    # 基础设施模块
├── 📂 yudao-module-bpm/      # 工作流模块
├── 📂 yudao-module-pay/      # 支付模块
├── 📂 yudao-module-mall/     # 商城模块
├── 📂 yudao-module-erp/      # ERP模块
├── 📂 yudao-module-crm/      # CRM模块
├── 📂 yudao-module-ai/       # AI大模型模块
├── 📂 yudao-module-mp/       # 微信公众号模块
├── 📂 yudao-module-member/   # 会员中心模块
├── 📂 yudao-module-report/   # 报表模块
├── 📂 yudao-module-iot/      # 物联网模块
├── 📂 yudao-ui/             # 前端项目
├── 📂 sql/                  # 数据库脚本
└── 📂 script/               # 部署脚本
```

## 💡 功能模块

<div align="center">

![功能分层](/.image/common/ruoyi-vue-pro-biz.png)

</div>

### 🎯 核心功能

<details>
<summary>🔐 系统管理</summary>

| 功能 | 描述 | 状态 |
|------|------|------|
| 👤 用户管理 | 用户信息维护、权限分配 | ✅ |
| 👥 角色管理 | 角色权限配置、数据权限 | ✅ |
| 📋 菜单管理 | 菜单配置、按钮权限 | ✅ |
| 🏢 部门管理 | 组织架构管理 | ✅ |
| 💼 岗位管理 | 岗位信息配置 | ✅ |
| 🏠 租户管理 | SaaS 多租户支持 | ✅ |
| 📝 操作日志 | 系统操作审计 | ✅ |
| 🔑 登录日志 | 登录安全监控 | ✅ |

</details>

<details>
<summary>🔄 工作流程</summary>

| 功能 | 描述 | 状态 |
|------|------|------|
| 🎨 流程设计器 | 可视化流程设计 | ✅ |
| 📝 表单设计器 | 动态表单配置 | ✅ |
| ✅ 审批管理 | 待办、已办、抄送 | ✅ |
| 🔀 流程实例 | 流程监控、统计 | ✅ |
| 📊 流程统计 | 效率分析报表 | ✅ |

**流程特性**：
- 🎯 会签/或签/依次审批
- 🔄 驳回/转办/委派/加签
- ⏰ 超时处理/自动提醒
- 🌳 条件分支/并行网关

</details>

<details>
<summary>🛒 商城系统</summary>

| 模块 | 功能描述 | 演示地址 |
|------|----------|----------|
| 🏪 商品管理 | SPU/SKU、分类、品牌 | [商城演示](https://doc.iocoder.cn/mall-preview/) |
| 📦 订单管理 | 下单、支付、发货、售后 | - |
| 🎁 营销工具 | 优惠券、拼团、秒杀 | - |
| 👥 会员系统 | 积分、等级、标签 | - |

</details>

<details>
<summary>🏭 ERP 系统</summary>

| 模块 | 功能描述 | 演示地址 |
|------|----------|----------|
| 📦 库存管理 | 入库、出库、调拨、盘点 | [ERP演示](https://doc.iocoder.cn/erp-preview/) |
| 🤝 采购管理 | 采购订单、供应商管理 | - |
| 💰 销售管理 | 销售订单、客户管理 | - |
| 💵 财务管理 | 应收应付、账目管理 | - |

</details>

<details>
<summary>👔 CRM 系统</summary>

| 模块 | 功能描述 | 演示地址 |
|------|----------|----------|
| 🎯 线索管理 | 线索收集、分配、跟进 | [CRM演示](https://doc.iocoder.cn/crm-preview/) |
| 👤 客户管理 | 客户档案、联系记录 | - |
| 💼 商机管理 | 销售机会、漏斗分析 | - |
| 📋 合同管理 | 合同签署、回款管理 | - |

</details>

<details>
<summary>🤖 AI 大模型</summary>

| 功能 | 描述 | 演示地址 |
|------|------|----------|
| 💬 AI 对话 | 智能问答、上下文理解 | [AI演示](https://doc.iocoder.cn/ai-preview/) |
| 🎨 AI 绘画 | 文生图、图生图 | - |
| ✍️ AI 写作 | 文案生成、内容创作 | - |
| 🔧 模型管理 | 多模型接入、配置管理 | - |

**支持的AI平台**：OpenAI、文心一言、通义千问、讯飞星火等

</details>

### 🔧 基础设施

| 功能 | 描述 | 特色 |
|------|------|------|
| 🎯 代码生成器 | 一键生成前后端代码 | 支持单表、树表、主子表 |
| 📊 数据报表 | 拖拽式报表设计器 | 支持图表、打印模板 |
| 📱 大屏设计 | 可视化大屏设计器 | 内置多种图表组件 |
| 📄 接口文档 | Swagger自动生成 | 在线调试、Mock数据 |
| 📝 数据库文档 | 自动生成数据库文档 | 支持多种格式导出 |
| 🔧 系统监控 | Java应用监控 | 性能指标、健康检查 |

## 🖥️ 演示图片

<details>
<summary>🖼️ 查看系统截图</summary>

### 🏠 系统首页
![首页](/.image/首页.jpg)

### 👤 用户管理
![用户管理](/.image/用户管理.jpg)

### 🔄 工作流程
![流程设计](/.image/流程模型-设计.jpg)

### 📊 数据报表
![报表设计器](/.image/报表设计器-数据报表.jpg)

</details>

## 🤝 项目关系

<div align="center">

![项目关系](/.image/common/yudao-roadmap.png)

### 🌟 生态项目

</div>

| 项目 | 描述 | Stars |
|------|------|-------|
| 🎯 **ruoyi-vue-pro** | Spring Boot 单体架构 | [![GitHub stars](https://img.shields.io/github/stars/YunaiV/ruoyi-vue-pro.svg?style=social)](https://github.com/YunaiV/ruoyi-vue-pro) |
| ☁️ **yudao-cloud** | Spring Cloud 微服务架构 | [![GitHub stars](https://img.shields.io/github/stars/YunaiV/yudao-cloud.svg?style=social)](https://github.com/YunaiV/yudao-cloud) |
| 📚 **SpringBoot-Labs** | Spring Boot 学习教程 | [![GitHub stars](https://img.shields.io/github/stars/yudaocode/SpringBoot-Labs.svg?style=social)](https://github.com/yudaocode/SpringBoot-Labs) |

## 📜 开源协议

<div align="center">

### 🎉 为什么推荐使用芋道？

</div>

| 优势 | 说明 |
|------|------|
| 📖 **MIT 协议** | 比 Apache 2.0 更宽松，个人企业 100% 免费使用 |
| 🔓 **完全开源** | 所有代码开源，不像其他项目只开源部分代码 |
| 📝 **代码质量** | 遵循阿里 Java 开发规范，注释覆盖率 40%+ |
| 🏆 **社区活跃** | GitHub 20k+ Stars，持续更新维护 |

![开源对比](/.image/common/project-vs.png)

## 💼 商务合作

<div align="center">

### 🤝 项目外包服务

我们提供专业的项目外包服务，团队包括：

| 角色 | 说明 |
|------|------|
| 🎯 项目经理 | 需求分析、进度管控 |
| 🏗️ 架构师 | 技术选型、架构设计 |
| 💻 开发工程师 | 前端、后端、测试 |
| 🚀 运维工程师 | 部署、监控、优化 |

**擅长领域**：商城、ERP、CRM、OA、物流、支付、IM等业务系统

**联系方式**：微信 **Aix9975**

</div>

## 🙏 致谢

感谢所有为芋道项目贡献代码的开发者们！

<div align="center">

### 💖 特别感谢

- 🌟 [RuoYi](https://gitee.com/y_project/RuoYi) - 原始框架基础
- 🎨 [Element Plus](https://element-plus.org/) - UI 组件库
- 🔧 [Spring Boot](https://spring.io/projects/spring-boot) - 核心框架
- 🌊 [Flowable](https://www.flowable.org/) - 工作流引擎

### 🌟 如果觉得项目不错，请 Star 支持一下！

[![Star History Chart](https://api.star-history.com/svg?repos=YunaiV/ruoyi-vue-pro&type=Date)](https://star-history.com/#YunaiV/ruoyi-vue-pro&Date)

</div>

---

<div align="center">

**🎯 让开发变得更简单、更高效、更有趣！**

Made with ❤️ by [芋道源码](https://www.iocoder.cn/)

</div>
