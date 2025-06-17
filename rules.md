# 若依Vue Pro 项目开发规范

## 📖 项目概述

本项目是基于Spring Boot + Vue.js的企业级后台管理系统，采用前后端分离架构，包含系统管理、工作流、支付、商城、CRM、ERP、AI大模型等多个业务模块。

## 🏗️ 架构规范

### 1. 模块化设计
- **核心框架**：`yudao-framework` - 提供基础框架功能
- **业务模块**：按业务领域划分，如 `yudao-module-system`、`yudao-module-bpm`等
- **服务端**：`yudao-server` - 主服务启动模块
- **依赖管理**：`yudao-dependencies` - 统一版本管理

### 2. 分层架构
```
Controller层 -> Service层 -> Manager层 -> DAO层
```
- **Controller**: 负责请求处理和响应
- **Service**: 业务逻辑处理
- **Manager**: 通用业务处理，可被多个Service调用
- **DAO**: 数据访问层

### 3. 包结构规范
```
cn.iocoder.yudao.module.{module}
├── controller    # 控制器
├── service       # 业务逻辑
├── manager       # 通用业务
├── dal           # 数据访问层
│   ├── dataobject # 数据对象
│   ├── mapper     # MyBatis Mapper
│   └── redis      # Redis操作
├── convert       # 对象转换
├── vo            # 视图对象
│   ├── request   # 请求对象
│   └── response  # 响应对象
└── enums         # 枚举类
```

## 💻 代码规范

### 1. 命名规范
- **类名**: 使用PascalCase，如 `UserController`、`UserService`
- **方法名**: 使用camelCase，如 `getUserById`、`createUser`
- **变量名**: 使用camelCase，如 `userId`、`userName`
- **常量名**: 使用UPPER_SNAKE_CASE，如 `MAX_PAGE_SIZE`
- **包名**: 使用小写，如 `controller`、`service`

### 2. 注解使用
- **Controller**: 使用`@RestController`、`@RequestMapping`
- **Service**: 使用`@Service`、`@Transactional`
- **Validation**: 使用`@Valid`、`@NotNull`、`@NotEmpty`等
- **API文档**: 使用`@Operation`、`@Parameter`等Swagger注解

### 3. 异常处理
- 使用自定义异常类继承`ServiceException`
- 统一异常码管理，在`ErrorCodeConstants`中定义
- Controller层不处理异常，由全局异常处理器统一处理

### 4. 日志规范
- 使用`@Slf4j`注解
- 关键操作记录INFO级别日志
- 异常记录ERROR级别日志
- 调试信息使用DEBUG级别

## 🔧 开发规范

### 1. 数据库规范
- **表名**: 使用小写下划线，如 `system_user`、`bpm_process`
- **字段名**: 使用小写下划线，如 `user_id`、`create_time`
- **主键**: 统一使用`id`作为主键，类型为`BIGINT`
- **审计字段**: 每个表包含`creator`、`create_time`、`updater`、`update_time`、`deleted`

### 2. API设计规范
- **RESTful**: 遵循RESTful API设计原则
- **URL**: 使用小写字母和连字符，如 `/admin-api/system/users`
- **HTTP方法**: GET(查询)、POST(新增)、PUT(修改)、DELETE(删除)
- **响应格式**: 统一使用`CommonResult`包装响应数据

### 3. 数据传输对象
- **DO (Data Object)**: 数据库实体对象，对应数据库表
- **VO (View Object)**: 视图对象，用于前端展示
- **DTO (Data Transfer Object)**: 数据传输对象，用于层间数据传输
- **Convert**: 使用MapStruct进行对象转换

### 4. 分页查询
- 统一使用`PageParam`作为分页参数基类
- 查询条件继承`PageParam`
- 返回结果使用`PageResult`包装

## 🛡️ 安全规范

### 1. 权限控制
- 使用`@PreAuthorize`注解进行权限校验
- 权限标识格式：`模块:操作`，如 `system:user:query`
- 数据权限使用`@DataPermission`注解

### 2. 参数校验
- Controller层使用`@Valid`进行参数校验
- 自定义校验注解放在`validation`包下
- 校验失败统一返回400错误码

### 3. 敏感数据
- 密码使用BCrypt加密存储
- 敏感信息不在日志中输出
- API接口需要进行签名校验（如支付相关）

## 📝 文档规范

### 1. 代码注释
- 类和方法使用JavaDoc注释
- 复杂业务逻辑添加行内注释
- 常量和枚举值添加注释说明

### 2. API文档
- 使用Swagger/OpenAPI规范
- Controller方法添加`@Operation`注解
- 请求参数添加`@Parameter`注解
- 响应模型添加`@Schema`注解

### 3. 数据库文档
- 表和字段添加COMMENT注释
- 使用工具自动生成数据库文档

## 🧪 测试规范

### 1. 单元测试
- Service层业务逻辑编写单元测试
- 测试覆盖率达到80%以上
- 使用`@Test`、`@MockBean`等注解

### 2. 集成测试
- Controller层编写集成测试
- 使用`@SpringBootTest`和`MockMvc`
- 测试核心业务流程

### 3. 测试数据
- 使用`@Sql`注解准备测试数据
- 测试后清理数据，避免相互影响

## 🚀 部署规范

### 1. 环境配置
- **开发环境**: application-dev.yaml
- **测试环境**: application-test.yaml  
- **生产环境**: application-prod.yaml

### 2. Docker化
- 提供Dockerfile和docker-compose.yml
- 镜像使用多阶段构建优化大小
- 配置健康检查

### 3. 监控告警
- 集成Spring Boot Actuator
- 使用SkyWalking进行链路追踪
- 配置日志收集和告警

## 📚 最佳实践

### 1. 性能优化
- 合理使用缓存（Redis）
- 数据库查询优化，避免N+1问题
- 异步处理耗时操作
- 使用连接池和对象池

### 2. 代码质量
- 遵循SOLID设计原则
- 使用设计模式解决复杂问题
- 定期进行代码Review
- 使用静态代码分析工具

### 3. 安全防护
- 防SQL注入、XSS攻击
- 使用HTTPS协议
- 接口限流和防刷
- 敏感操作日志记录

### 4. 多租户设计
- 使用`@TenantIgnore`忽略租户过滤
- 租户数据隔离
- 租户配置管理

## 🔄 版本管理

### 1. Git规范
- **分支策略**: Git Flow工作流
- **提交消息**: 格式为 `type(scope): description`
- **Tag**: 版本发布使用语义化版本号

### 2. 发布流程
- 功能开发在feature分支
- 合并到develop分支进行集成测试
- 发布时合并到master分支
- 使用CI/CD自动化部署

## 📋 检查清单

### 开发完成检查
- [ ] 代码符合命名规范
- [ ] 添加必要的注释和文档
- [ ] 编写单元测试
- [ ] API文档更新
- [ ] 权限配置正确
- [ ] 异常处理完善
- [ ] 日志记录适当

### 发布前检查
- [ ] 功能测试通过
- [ ] 性能测试达标
- [ ] 安全测试通过
- [ ] 部署文档更新
- [ ] 监控配置完成
- [ ] 回滚方案准备

---

> 📖 本规范会随着项目发展持续更新，请及时关注最新版本。
> 
> 💡 如有疑问或建议，请在项目Issues中提出。