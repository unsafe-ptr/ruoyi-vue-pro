# 💻 开发指南

本文档为芋道项目的开发者提供详细的开发环境配置、代码规范和最佳实践指导。

## 🛠 开发环境配置

### IDE 配置

#### IntelliJ IDEA（推荐）
1. **导入项目**
   ```
   File → New → Project from Existing Sources
   选择项目根目录的 pom.xml
   ```

2. **安装必需插件**
   - Lombok Plugin
   - MyBatis X
   - Free MyBatis Plugin
   - Maven Helper

3. **代码格式化配置**
   - 导入 `script/idea/settings.jar` 配置文件
   - 设置编码为 UTF-8
   - 设置 Tab 为 4 个空格

4. **启动配置**
   ```
   Main class: cn.iocoder.yudao.server.YudaoServerApplication
   VM options: -Dspring.profiles.active=local
   ```

### 数据库配置

#### MySQL 配置
```sql
-- 创建数据库
CREATE DATABASE ruoyi_vue_pro DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- 创建用户（可选）
CREATE USER 'yudao'@'localhost' IDENTIFIED BY 'yudao123';
GRANT ALL PRIVILEGES ON ruoyi_vue_pro.* TO 'yudao'@'localhost';
FLUSH PRIVILEGES;
```

#### 多数据库支持
项目支持多种数据库，配置文件位于 `sql/` 目录：
- MySQL: `sql/mysql/`
- Oracle: `sql/oracle/`
- PostgreSQL: `sql/postgresql/`
- SQL Server: `sql/sqlserver/`
- 国产数据库：达梦、人大金仓、openGauss 等

### 环境配置文件

#### 开发环境 (application-local.yaml)
```yaml
spring:
  datasource:
    dynamic:
      primary: master
      datasource:
        master:
          url: jdbc:mysql://127.0.0.1:3306/ruoyi_vue_pro?useSSL=false&serverTimezone=Asia/Shanghai
          username: root
          password: 123456
  redis:
    host: 127.0.0.1
    port: 6379
    database: 1

logging:
  level:
    cn.iocoder.yudao.module: debug
```

## 📐 代码规范

### 包结构规范
```
cn.iocoder.yudao.module.{module}
├── controller/     # 控制器层
├── service/        # 业务逻辑层
│   └── impl/      # 实现类
├── dal/           # 数据访问层
│   ├── dataobject/ # 数据对象（DO）
│   └── mysql/     # MyBatis Mapper
├── convert/       # 对象转换类
├── vo/            # 视图对象
│   ├── req/       # 请求对象
│   └── resp/      # 响应对象
└── enums/         # 枚举类
```

### 命名规范

#### 类命名
- Controller: `{模块}{功能}Controller`
- Service: `{模块}{功能}Service`
- ServiceImpl: `{模块}{功能}ServiceImpl`
- Mapper: `{模块}{功能}Mapper`
- DO: `{模块}{功能}DO`
- VO: `{模块}{功能}{Req/Resp}VO`

#### 方法命名
- 查询列表: `get{模块}Page`
- 查询详情: `get{模块}`
- 新增: `create{模块}`
- 修改: `update{模块}`
- 删除: `delete{模块}`

### 注释规范

#### 类注释
```java
/**
 * {功能描述} Service 接口
 *
 * @author {作者}
 */
```

#### 方法注释
```java
/**
 * 创建{功能}
 *
 * @param createReqVO 创建信息
 * @return 编号
 */
```

### 异常处理

#### 自定义异常
```java
// 定义错误码
ErrorCodeConstants.USER_NOT_EXISTS = new ErrorCode(1_002_001_000, "用户不存在");

// 抛出异常
throw exception(USER_NOT_EXISTS);
```

#### 统一返回格式
```java
// 成功返回
return success(data);

// 失败返回（会被全局异常处理器处理）
throw exception(ERROR_CODE);
```

## 🔧 开发工具

### 代码生成器
1. 访问：http://localhost:48080
2. 系统管理 → 代码生成
3. 选择数据表 → 生成代码
4. 支持生成：Controller、Service、Mapper、Vue页面

### 数据库文档生成
```bash
# 访问数据库文档
http://localhost:48080/doc/db
```

### API 文档
```bash
# Swagger UI
http://localhost:48080/swagger-ui

# Knife4j 增强版
http://localhost:48080/doc.html
```

## 🧪 单元测试

### 测试规范
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserServiceImplTest extends BaseDbUnitTest {

    @Resource
    private UserService userService;

    @Test
    public void testCreateUser_success() {
        // given
        UserCreateReqVO reqVO = randomPojo(UserCreateReqVO.class);
        
        // when
        Long userId = userService.createUser(reqVO);
        
        // then
        assertNotNull(userId);
        UserDO user = userService.getUser(userId);
        assertPojoEquals(reqVO, user);
    }
}
```

### 运行测试
```bash
# 运行所有测试
mvn test

# 运行特定模块测试
mvn test -pl yudao-module-system

# 跳过测试
mvn install -Dmaven.test.skip=true
```

## 🔒 权限开发

### 菜单权限
```java
@PreAuthorize("@ss.hasPermission('system:user:query')")
@GetMapping("/page")
public CommonResult<PageResult<UserRespVO>> getUserPage(@Valid UserPageReqVO pageVO) {
    // 实现代码
}
```

### 数据权限
```java
@DataPermission(enable = false) // 禁用数据权限
public List<UserDO> getUserList() {
    // 实现代码
}
```

## 🔄 工作流开发

### 流程定义
1. 工作流程 → 流程模型 → 新建流程
2. 选择设计器类型：BPMN 或 仿钉钉
3. 设计流程图并发布

### 业务集成
```java
@Service
public class LeaveServiceImpl implements LeaveService {

    @Resource
    private ProcessInstanceApi processInstanceApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createLeave(LeaveCreateReqVO createReqVO) {
        // 1. 插入请假申请
        LeaveDO leave = LeaveConvert.INSTANCE.convert(createReqVO);
        leaveMapper.insert(leave);

        // 2. 发起工作流
        String processInstanceId = processInstanceApi.createProcessInstance(
                createReqVO.getUserId(), 
                "leave", 
                createReqVO);

        // 3. 更新流程实例编号
        leave.setProcessInstanceId(processInstanceId);
        leaveMapper.updateById(leave);

        return leave.getId();
    }
}
```

## 📊 性能优化

### 数据库优化
1. **索引优化**：为常用查询字段添加索引
2. **SQL优化**：避免 N+1 查询，使用批量操作
3. **分页查询**：大数据量使用游标分页

### 缓存优化
```java
@Cacheable(value = "user", key = "#id")
public UserDO getUser(Long id) {
    return userMapper.selectById(id);
}

@CacheEvict(value = "user", key = "#id")
public void deleteUser(Long id) {
    userMapper.deleteById(id);
}
```

### 异步处理
```java
@Async
public void sendEmail(String email, String content) {
    // 异步发送邮件
}
```

## 🚀 部署优化

### JVM 参数优化
```bash
java -jar yudao-server.jar \
  -Xms2g -Xmx2g \
  -XX:+UseG1GC \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=/logs/heapdump.hprof
```

### 监控配置
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: always
```

## 🐛 常见问题

### 1. 编译问题
**问题**：Lombok 注解不生效
**解决**：安装 Lombok 插件并重启 IDE

### 2. 数据库问题
**问题**：时区问题导致时间错误
**解决**：URL 添加 `serverTimezone=Asia/Shanghai`

### 3. 缓存问题
**问题**：Redis 连接超时
**解决**：检查网络连接和 Redis 配置

### 4. 权限问题
**问题**：接口返回 403 无权限
**解决**：检查用户角色权限配置

## 📚 学习资源

- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [MyBatis Plus 官方文档](https://mp.baomidou.com/)
- [Spring Security 官方文档](https://spring.io/projects/spring-security)
- [芋道源码](https://www.iocoder.cn/) - 作者的技术博客