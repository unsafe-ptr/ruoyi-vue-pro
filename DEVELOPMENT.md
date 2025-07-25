# 💻 开发指南

> **快速上手企业级开发，掌握最佳实践！**

<div align="center">

[![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ%20IDEA-推荐-blue.svg)](https://www.jetbrains.com/idea/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Code Style](https://img.shields.io/badge/代码规范-阿里巴巴-orange.svg)](https://github.com/alibaba/p3c)

</div>

## 🛠 开发环境搭建

### IDE 推荐配置

#### ⚡ IntelliJ IDEA（强烈推荐）

```bash
# 1. 必装插件
- Lombok Plugin
- MyBatis X
- Maven Helper
- Alibaba Java Coding Guidelines

# 2. 导入代码风格
导入：script/idea/settings.jar

# 3. 启动配置
Main class: cn.iocoder.yudao.server.YudaoServerApplication
VM options: -Dspring.profiles.active=local
```

<details>
<summary>🔧 <strong>IDEA 优化配置</strong></summary>

```bash
# VM 配置优化
-Xmx2048m
-XX:+UseConcMarkSweepGC
-XX:SoftRefLRUPolicyMSPerMB=50

# 编码设置
File → Settings → Editor → File Encodings
设置为 UTF-8

# 自动导包
File → Settings → Editor → General → Auto Import
勾选所有选项
```

</details>

## 🚀 核心开发技能

### 🎯 项目结构理解

```
yudao-module-{module}/
├── controller/         # 控制器层 - 接收请求
├── service/           # 业务逻辑层 - 核心业务
│   └── impl/         # 实现类
├── dal/              # 数据访问层
│   ├── dataobject/   # 数据对象(DO)
│   └── mysql/        # MyBatis Mapper
├── convert/          # 对象转换类
├── vo/               # 视图对象
│   ├── req/         # 请求对象
│   └── resp/        # 响应对象
└── enums/            # 枚举类
```

### 🔥 必会开发模式

#### 1. CRUD 标准开发流程

```java
// 1. 创建 DO (Data Object)
@TableName("system_user")
public class UserDO extends BaseDO {
    private Long id;
    private String username;
    // ...
}

// 2. 创建 Mapper
@Mapper
public interface UserMapper extends BaseMapperX<UserDO> {
    default PageResult<UserDO> selectPage(UserPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<UserDO>()
                .likeIfPresent(UserDO::getUsername, reqVO.getUsername())
                .betweenIfPresent(UserDO::getCreateTime, reqVO.getCreateTime()));
    }
}

// 3. 创建 Service
@Service
public class UserServiceImpl implements UserService {
    
    @Override
    public Long createUser(UserCreateReqVO createReqVO) {
        // 校验用户名唯一性
        validateUserUnique(createReqVO.getUsername());
        
        // 插入用户
        UserDO user = UserConvert.INSTANCE.convert(createReqVO);
        userMapper.insert(user);
        return user.getId();
    }
}

// 4. 创建 Controller
@RestController
@RequestMapping("/admin-api/system/user")
public class UserController {
    
    @PostMapping("/create")
    @PreAuthorize("@ss.hasPermission('system:user:create')")
    public CommonResult<Long> createUser(@Valid @RequestBody UserCreateReqVO createReqVO) {
        return success(userService.createUser(createReqVO));
    }
}
```

#### 2. 统一异常处理

```java
// 定义错误码
public interface ErrorCodeConstants {
    ErrorCode USER_NOT_EXISTS = new ErrorCode(1_002_001_000, "用户不存在");
    ErrorCode USERNAME_DUPLICATE = new ErrorCode(1_002_001_001, "用户名已存在");
}

// 使用异常
public void validateUserUnique(String username) {
    if (userMapper.selectByUsername(username) != null) {
        throw exception(USERNAME_DUPLICATE);
    }
}
```

#### 3. 权限控制

```java
// 菜单权限
@PreAuthorize("@ss.hasPermission('system:user:query')")

// 数据权限
@DataPermission(enable = false) // 禁用数据权限
@DataPermission(enable = true, includeScope = DeptDataPermissionRespVO.class)

// 多租户
@TenantIgnore // 忽略多租户
```

## 🧰 开发神器

### 代码生成器

```bash
# 1. 访问代码生成
http://localhost:48080 → 系统管理 → 代码生成

# 2. 一键生成
- Controller (控制器)
- Service (业务层)  
- Mapper (数据层)
- Vue 页面
- SQL 脚本
```

### 接口调试

```bash
# Swagger 文档
http://localhost:48080/doc.html

# 接口测试
http://localhost:48080/swagger-ui

# 数据库文档
http://localhost:48080/doc/db
```

### 实用工具类

```java
// 1. 集合工具
CollUtil.isEmpty(list)
CollUtil.convertList(list, User::getName)

// 2. 字符串工具  
StrUtil.isBlank(str)
StrUtil.format("用户{}不存在", username)

// 3. 日期工具
LocalDateTimeUtil.now()
LocalDateTimeUtil.parse("2023-01-01 12:00:00")

// 4. Bean 拷贝
BeanUtil.copyProperties(source, target)

// 5. 分页工具
PageUtils.build(pageNo, pageSize)
```

## 🧪 测试最佳实践

### 单元测试模板

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserServiceImplTest extends BaseDbUnitTest {

    @Resource
    private UserService userService;

    @Test
    public void testCreateUser_success() {
        // given - 准备数据
        UserCreateReqVO reqVO = randomPojo(UserCreateReqVO.class, 
            o -> o.setUsername("test"));
        
        // when - 执行操作
        Long userId = userService.createUser(reqVO);
        
        // then - 验证结果
        assertNotNull(userId);
        UserDO user = userService.getUser(userId);
        assertPojoEquals(reqVO, user);
    }
    
    @Test
    public void testCreateUser_usernameDuplicate() {
        // given
        insertUser(o -> o.setUsername("test"));
        UserCreateReqVO reqVO = randomPojo(UserCreateReqVO.class, 
            o -> o.setUsername("test"));
        
        // when & then
        assertServiceException(() -> userService.createUser(reqVO), 
            USERNAME_DUPLICATE);
    }
}
```

### Mock 测试技巧

```java
@MockBean
private RedisTemplate<String, String> redisTemplate;

@Test
public void testWithMock() {
    // Mock 返回值
    when(redisTemplate.opsForValue().get("key")).thenReturn("value");
    
    // 验证调用
    verify(redisTemplate).opsForValue();
}
```

## 🎨 代码规范速查

### 命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| Controller | `{模块}{功能}Controller` | `UserController` |
| Service | `{模块}{功能}Service` | `UserService` |
| DO | `{模块}{功能}DO` | `UserDO` |
| VO | `{模块}{功能}{类型}VO` | `UserCreateReqVO` |

### 方法命名

| 操作 | 命名 | 示例 |
|------|------|------|
| 查询分页 | `get{模块}Page` | `getUserPage` |
| 查询详情 | `get{模块}` | `getUser` |
| 创建 | `create{模块}` | `createUser` |
| 更新 | `update{模块}` | `updateUser` |
| 删除 | `delete{模块}` | `deleteUser` |

## 🔧 常用开发技巧

### 1. 快速生成测试数据

```java
// 使用 RandomUtils 生成测试数据
UserDO user = randomPojo(UserDO.class, o -> {
    o.setId(null); // 不设置ID
    o.setUsername("test" + randomString()); // 随机用户名
});
```

### 2. 数据库操作技巧

```java
// 批量插入
userMapper.insertBatch(userList);

// 条件构造器
LambdaQueryWrapperX<UserDO> query = new LambdaQueryWrapperX<UserDO>()
    .eqIfPresent(UserDO::getStatus, reqVO.getStatus())
    .likeIfPresent(UserDO::getUsername, reqVO.getUsername())
    .betweenIfPresent(UserDO::getCreateTime, reqVO.getCreateTime());
```

### 3. 缓存使用技巧

```java
// Spring Cache
@Cacheable(value = "user", key = "#id")
public UserDO getUser(Long id) {
    return userMapper.selectById(id);
}

// Redis 工具类
stringRedisTemplate.opsForValue().set("key", "value", Duration.ofMinutes(30));
```

### 4. 异步处理

```java
@Async("taskExecutor")
public CompletableFuture<Void> sendEmailAsync(String email, String content) {
    // 异步发送邮件
    emailService.send(email, content);
    return CompletableFuture.completedFuture(null);
}
```

## 🐛 调试技巧

### 1. 日志调试

```java
// 使用 Slf4j
@Slf4j
@Service
public class UserServiceImpl {
    
    public void createUser(UserCreateReqVO reqVO) {
        log.debug("创建用户，参数：{}", reqVO);
        // 业务逻辑
        log.info("用户创建成功，ID：{}", user.getId());
    }
}
```

### 2. 远程调试

```bash
# 启动时加上调试参数
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -jar app.jar

# IDEA 配置远程调试
Run → Edit Configurations → + → Remote JVM Debug
```

### 3. 性能分析

```bash
# JVM 参数
-XX:+PrintGCDetails
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/logs/heapdump.hprof

# 分析工具
jstack <pid>  # 线程栈
jmap -dump:format=b,file=heap.dump <pid>  # 内存快照
```

## 🚀 性能优化建议

### 1. SQL 优化

```sql
-- 添加索引
ALTER TABLE system_user ADD INDEX idx_username (username);

-- 避免 SELECT *
SELECT id, username, status FROM system_user WHERE status = 1;

-- 使用 LIMIT
SELECT * FROM system_user ORDER BY id DESC LIMIT 10;
```

### 2. 缓存策略

```java
// 缓存热点数据
@Cacheable(value = "user", key = "#id", condition = "#id != null")

// 缓存失效策略
@CacheEvict(value = "user", key = "#user.id")

// 缓存更新
@CachePut(value = "user", key = "#user.id")
```

### 3. 连接池配置

```yaml
spring:
  datasource:
    druid:
      initial-size: 5
      max-active: 20
      min-idle: 5
      max-wait: 60000
```

## 📚 学习资源

| 资源 | 链接 | 说明 |
|------|------|------|
| 🎥 视频教程 | [B站教程](https://doc.iocoder.cn/video/) | 项目讲解视频 |
| 📖 在线文档 | [官方文档](https://doc.iocoder.cn/) | 详细开发文档 |
| 💬 技术交流 | QQ群：3147719 | 开发问题交流 |
| 🐛 问题反馈 | [Gitee Issues](https://gitee.com/zhijiantianya/ruoyi-vue-pro/issues) | Bug反馈 |

---

**🎯 快速定位问题**

- 启动问题 → 检查端口、JDK版本、数据库连接
- 权限问题 → 检查 `@PreAuthorize` 注解和角色配置  
- 数据问题 → 检查多租户、数据权限配置
- 性能问题 → 开启SQL日志、检查索引、分析慢查询