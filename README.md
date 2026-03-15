# Spring Boot Template Project

基于 Spring Boot 3.x 的企业级后端开发模板，集成主流技术栈与最佳实践，助力快速构建高质量应用。

## 目录

- [项目概述](#项目概述)
- [技术栈](#技术栈)
- [项目结构](#项目结构)
- [快速开始](#快速开始)
- [环境配置](#环境配置)
- [核心功能](#核心功能)
- [接口开发流程](#接口开发流程)
- [测试指南](#测试指南)
- [API文档](#api文档)
- [常见问题](#常见问题)
- [贡献指南](#贡献指南)

---

## 项目概述

### 设计理念

本项目采用**分层架构**与**约定优于配置**的设计思想，遵循 SOLID 原则，提供清晰的项目结构与代码规范。模板旨在：

- **开箱即用**：集成企业级开发常用组件，减少重复配置
- **最佳实践**：融入行业认可的设计模式与编码规范
- **可扩展性**：模块化设计，便于功能扩展与定制
- **安全优先**：内置完善的认证授权机制

### 主要功能

| 功能模块 | 说明 |
|---------|------|
| 用户管理 | 用户注册、登录、信息维护、状态管理 |
| 角色权限 | RBAC权限模型，角色与权限的灵活配置 |
| 文件上传 | 支持单文件/批量上传，按日期分目录存储 |
| 认证授权 | JWT Token认证，Redis会话管理 |
| 操作日志 | 请求日志记录，便于问题追踪 |

### 适用场景

- 企业级管理系统后端
- 微服务基础模板
- 快速原型开发
- 学习与教学示例

---

## 技术栈

### 核心框架

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.2.0 | 基础框架 |
| Spring Security | 6.x | 安全框架 |
| MyBatis-Plus | 3.5.5 | ORM框架 |
| MySQL | 8.0+ | 关系型数据库 |
| Redis | 7.0+ | 缓存数据库 |

### 组件依赖

| 组件 | 版本 | 说明 |
|------|------|------|
| JWT (jjwt) | 0.12.3 | Token生成与验证 |
| Hutool | 5.8.24 | Java工具类库 |
| Lombok | 1.18.30 | 代码简化 |
| FastJSON2 | 2.0.43 | JSON处理 |
| SpringDoc | 2.3.0 | API文档生成 |

---

## 项目结构

```
src/main/java/com/example/back/
├── common/                     # 通用模块
│   ├── BaseEntity.java         # 实体基类
│   ├── BusinessException.java  # 业务异常
│   ├── GlobalExceptionHandler.java  # 全局异常处理
│   ├── PageResult.java         # 分页结果封装
│   ├── Result.java             # 统一响应结果
│   └── ResultCode.java         # 响应状态码
├── config/                     # 配置类
│   ├── FileUploadProperties.java  # 文件上传配置
│   ├── MybatisPlusConfig.java  # MyBatis-Plus配置
│   ├── RedisConfig.java        # Redis配置
│   ├── SecurityConfig.java     # Security安全配置
│   └── WebMvcConfig.java       # Web MVC配置
├── controller/                 # 控制器层
│   ├── AuthController.java     # 认证控制器
│   ├── FileController.java     # 文件控制器
│   └── UserController.java     # 用户控制器
├── dto/                        # 数据传输对象
│   ├── FileQueryDTO.java
│   ├── FileUploadDTO.java
│   ├── LoginDTO.java
│   ├── PageDTO.java
│   ├── PasswordUpdateDTO.java
│   ├── PermissionDTO.java
│   ├── RoleDTO.java
│   ├── RolePermissionDTO.java
│   ├── UserDTO.java
│   ├── UserProfileUpdateDTO.java
│   ├── UserQueryDTO.java
│   ├── UserRegisterDTO.java
│   └── UserRoleDTO.java
├── entity/                     # 实体类
│   ├── Permission.java
│   ├── Role.java
│   ├── RolePermission.java
│   ├── SysFile.java
│   ├── User.java
│   └── UserRole.java
├── mapper/                     # Mapper接口
│   ├── PermissionMapper.java
│   ├── RoleMapper.java
│   ├── RolePermissionMapper.java
│   ├── SysFileMapper.java
│   ├── UserMapper.java
│   └── UserRoleMapper.java
├── security/                   # 安全模块
│   ├── JwtAccessDeniedHandler.java
│   ├── JwtAuthenticationEntryPoint.java
│   ├── JwtAuthenticationFilter.java
│   ├── JwtTokenProvider.java
│   ├── LoginUser.java
│   └── UserDetailsServiceImpl.java
├── service/                    # 服务层
│   ├── impl/                   # 服务实现
│   │   ├── FileServiceImpl.java
│   │   ├── PermissionServiceImpl.java
│   │   ├── RoleServiceImpl.java
│   │   └── UserServiceImpl.java
│   ├── FileService.java
│   ├── PermissionService.java
│   ├── RoleService.java
│   └── UserService.java
├── vo/                         # 视图对象
│   ├── FileVO.java
│   ├── LoginLogVO.java
│   ├── MenuVO.java
│   ├── PermissionVO.java
│   ├── RoleVO.java
│   ├── TokenVO.java
│   ├── UserInfoVO.java
│   └── UserVO.java
└── BackApplication.java        # 启动类

src/main/resources/
├── db/
│   └── schema.sql              # 数据库初始化脚本
├── mapper/
│   └── UserMapper.xml          # Mapper XML文件
├── static/
│   └── index.html              # API测试页面
└── application.yml             # 应用配置
```

### 分层职责

| 层级 | 职责 | 示例 |
|------|------|------|
| Controller | 接收请求、参数校验、调用服务、返回响应 | `UserController` |
| Service | 业务逻辑处理、事务管理 | `UserServiceImpl` |
| Mapper | 数据库访问、SQL映射 | `UserMapper` |
| Entity | 数据库表映射 | `User` |
| DTO | 接收请求参数 | `UserDTO` |
| VO | 返回响应数据 | `UserVO` |

---

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Redis 7.0+

### 启动步骤

```bash
# 1. 克隆项目
git clone <repository-url>
cd back

# 2. 创建数据库并执行初始化脚本
mysql -u root -p < src/main/resources/db/schema.sql

# 3. 修改配置文件
# 编辑 src/main/resources/application.yml
# 配置数据库连接和Redis连接

# 4. 启动Redis
redis-server

# 5. 编译项目
./mvnw clean compile

# 6. 运行项目
./mvnw spring-boot:run

# 7. 访问测试页面
# 浏览器打开 http://localhost:8080
```

### 默认账号

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | 123456 | 超级管理员 |

---

## 环境配置

### 配置文件说明

```yaml
# application.yml
server:
  port: 8080                    # 服务端口

spring:
  datasource:                   # 数据库配置
    url: jdbc:mysql://localhost:3306/template_db
    username: root
    password: root
    
  data:
    redis:                      # Redis配置
      host: localhost
      port: 6379
      database: 0

jwt:                            # JWT配置
  secret: your-secret-key       # 密钥（生产环境请修改）
  access-token-expiration: 7200000   # Token有效期（毫秒）

file:                           # 文件上传配置
  upload:
    path: ./uploads             # 存储路径
    max-size: 10485760          # 最大文件大小（10MB）
```

### 环境变量配置

支持通过环境变量覆盖配置：

```bash
# Linux/macOS
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=template_db
export DB_USER=root
export DB_PASS=root
export REDIS_HOST=localhost
export REDIS_PORT=6379

# Windows PowerShell
$env:DB_HOST="localhost"
$env:DB_PORT="3306"
```

在 `application.yml` 中引用：

```yaml
spring:
  datasource:
    url: jdbc:mysql://${DB_HOST:localhost}:${DB_PORT:3306}/${DB_NAME:template_db}
    username: ${DB_USER:root}
    password: ${DB_PASS:root}
```

### 多环境配置

```bash
# 开发环境
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# 生产环境
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

创建 `application-dev.yml` 和 `application-prod.yml` 分别配置不同环境。

---

## 核心功能

### 认证授权

> **⚠️ 重要提示**
> 
> 若当前项目部署或使用场景中不需要实现权限认证功能，可直接忽略本章节中与权限认证相关的配置说明、代码实现及依赖项。如需移除权限认证模块，可删除以下内容：
> - `spring-boot-starter-security` 依赖
> - `security/` 目录下的所有安全相关类
> - `SecurityConfig.java` 配置类
> - JWT 相关依赖和配置

#### 登录流程

```
┌─────────┐    ┌───────────────┐    ┌────────────┐    ┌───────┐
│  Client │───>│ AuthController│───>│ UserService│───>│ MySQL │
└─────────┘    └───────────────┘    └────────────┘    └───────┘
                      │                    │
                      │                    ▼
                      │               ┌──────────┐
                      │               │  Redis   │ (存储Token)
                      │               └──────────┘
                      ▼
              返回 JWT Token
```

#### Token验证

每次请求携带 Token：

```http
Authorization: Bearer <access_token>
```

#### 代码示例

```java
// 登录
POST /api/auth/login
{
    "username": "admin",
    "password": "123456"
}

// 响应
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
        "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
        "expiresIn": 7200
    }
}
```

### 文件上传

#### 存储结构

```
uploads/
├── avatar/                     # 头像模块
│   └── 2024/03/14/            # 按日期分目录
│       └── uuid.jpg
├── document/                   # 文档模块
│   └── 2024/03/14/
│       └── uuid.pdf
└── 2024/03/14/                # 默认目录
    └── uuid.png
```

#### 上传示例

```bash
# 单文件上传
curl -X POST http://localhost:8080/api/files/upload \
  -H "Authorization: Bearer <token>" \
  -F "file=@/path/to/image.jpg" \
  -F "module=avatar"
```

### 权限控制

> **⚠️ 重要提示**
> 
> 权限控制功能依赖于认证授权模块。若不需要权限认证功能，可忽略本章节内容。

#### 权限注解

```java
// 方法级权限控制
@PreAuthorize("hasAuthority('user:add')")
@PostMapping
public Result<Long> addUser(@RequestBody UserDTO userDTO) {
    // ...
}

// 角色级权限控制
@PreAuthorize("hasRole('ADMIN')")
@DeleteMapping("/{id}")
public Result<Void> deleteUser(@PathVariable Long id) {
    // ...
}
```

---

## 接口开发流程

### 1. 需求分析

以"部门管理"为例：

| 项目 | 内容 |
|------|------|
| 功能 | 部门的增删改查 |
| 字段 | id, deptName, deptCode, parentId, sort, status |
| 权限 | dept:list, dept:add, dept:edit, dept:delete |

### 2. 数据库设计

```sql
-- 创建部门表
CREATE TABLE `sys_dept` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `dept_name` VARCHAR(50) NOT NULL COMMENT '部门名称',
    `dept_code` VARCHAR(50) NOT NULL COMMENT '部门编码',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父部门ID',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `status` TINYINT DEFAULT 1 COMMENT '状态',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_dept_code` (`dept_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';
```

### 3. 创建实体类

```java
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dept")
public class Dept extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String deptName;
    private String deptCode;
    private Long parentId;
    private Integer sort;
    private Integer status;
}
```

### 4. 创建DTO/VO

```java
// DeptDTO.java
@Data
public class DeptDTO implements Serializable {
    private Long id;
    
    @NotBlank(message = "部门名称不能为空")
    @Size(max = 50, message = "部门名称长度不能超过50")
    private String deptName;
    
    @NotBlank(message = "部门编码不能为空")
    @Size(max = 50, message = "部门编码长度不能超过50")
    private String deptCode;
    
    private Long parentId;
    private Integer sort;
    private Integer status;
}

// DeptVO.java
@Data
public class DeptVO implements Serializable {
    private Long id;
    private String deptName;
    private String deptCode;
    private Long parentId;
    private Integer sort;
    private Integer status;
    private LocalDateTime createTime;
    private List<DeptVO> children;
}
```

### 5. 创建Mapper

```java
@Mapper
public interface DeptMapper extends BaseMapper<Dept> {
    // BaseMapper已提供基础CRUD
    // 复杂查询可在此定义
}
```

### 6. 创建Service

```java
// DeptService.java
public interface DeptService extends IService<Dept> {
    PageResult<DeptVO> getPageList(DeptQueryDTO queryDTO);
    Long addDept(DeptDTO deptDTO);
    void updateDept(DeptDTO deptDTO);
    void deleteDept(Long id);
    List<DeptVO> getDeptTree();
}

// DeptServiceImpl.java
@Slf4j
@Service
@RequiredArgsConstructor
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {
    
    @Override
    public PageResult<DeptVO> getPageList(DeptQueryDTO queryDTO) {
        Page<Dept> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());
        LambdaQueryWrapper<Dept> wrapper = new LambdaQueryWrapper<>();
        
        if (StrUtil.isNotBlank(queryDTO.getDeptName())) {
            wrapper.like(Dept::getDeptName, queryDTO.getDeptName());
        }
        wrapper.orderByAsc(Dept::getSort);
        
        Page<Dept> deptPage = this.page(page, wrapper);
        
        List<DeptVO> voList = deptPage.getRecords().stream()
                .map(dept -> BeanUtil.copyProperties(dept, DeptVO.class))
                .toList();
        
        return new PageResult<>(voList, deptPage.getTotal(), 
                deptPage.getSize(), deptPage.getCurrent());
    }
    
    // 其他方法实现...
}
```

### 7. 创建Controller

```java
@Slf4j
@RestController
@RequestMapping("/api/depts")
@RequiredArgsConstructor
@Tag(name = "部门管理", description = "部门相关接口")
public class DeptController {

    private final DeptService deptService;

    @GetMapping("/page")
    @Operation(summary = "分页查询部门")
    public Result<PageResult<DeptVO>> getPageList(DeptQueryDTO queryDTO) {
        return Result.success(deptService.getPageList(queryDTO));
    }

    @PostMapping
    @Operation(summary = "新增部门")
    public Result<Long> addDept(@Valid @RequestBody DeptDTO deptDTO) {
        return Result.success(deptService.addDept(deptDTO));
    }

    @PutMapping
    @Operation(summary = "更新部门")
    public Result<Void> updateDept(@Valid @RequestBody DeptDTO deptDTO) {
        deptService.updateDept(deptDTO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除部门")
    public Result<Void> deleteDept(@PathVariable Long id) {
        deptService.deleteDept(id);
        return Result.success();
    }
}
```

### 8. 配置权限

在 `SecurityConfig.java` 中添加接口权限：

```java
.authorizeHttpRequests(auth -> auth
    // 添加公开接口
    .requestMatchers("/api/depts/tree").permitAll()
    // 其他需要认证
    .anyRequest().authenticated())
```

---

## 测试指南

### 单元测试

#### 测试类规范

```java
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("测试用户登录-成功场景")
    void testLogin_Success() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("admin");
        loginDTO.setPassword("123456");

        TokenVO tokenVO = userService.login(loginDTO);

        assertNotNull(tokenVO);
        assertNotNull(tokenVO.getAccessToken());
    }

    @Test
    @DisplayName("测试用户登录-密码错误")
    void testLogin_WrongPassword() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("admin");
        loginDTO.setPassword("wrongpassword");

        assertThrows(BusinessException.class, () -> {
            userService.login(loginDTO);
        });
    }
}
```

#### 执行测试

```bash
# 执行所有测试
./mvnw test

# 执行指定测试类
./mvnw test -Dtest=UserServiceTest

# 执行指定测试方法
./mvnw test -Dtest=UserServiceTest#testLogin_Success

# 跳过测试
./mvnw package -DskipTests
```

### 集成测试

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @BeforeEach
    void setUp() throws Exception {
        // 登录获取Token
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("admin");
        loginDTO.setPassword("123456");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(response);
        token = jsonNode.path("data").path("accessToken").asText();
    }

    @Test
    void testGetUserInfo() throws Exception {
        mockMvc.perform(get("/api/users/info")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.username").value("admin"));
    }
}
```

### 测试覆盖率

```bash
# 生成覆盖率报告
./mvnw jacoco:report

# 报告位置
# target/site/jacoco/index.html
```

---

## API文档

### Swagger UI

启动项目后访问：`http://localhost:8080/swagger-ui.html`

### API测试页面

访问：`http://localhost:8080/`

内置API测试控制台，支持：
- 用户登录认证
- Token自动管理
- 所有接口测试
- 文件上传下载

### 接口列表

#### 认证接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/auth/login | 用户登录 |
| POST | /api/auth/refresh | 刷新Token |
| POST | /api/auth/logout | 用户登出 |

#### 用户接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/users/info | 获取当前用户信息 |
| GET | /api/users/page | 分页查询用户 |
| GET | /api/users/{id} | 获取用户详情 |
| POST | /api/users | 新增用户 |
| PUT | /api/users | 更新用户 |
| DELETE | /api/users/{id} | 删除用户 |
| PUT | /api/users/{id}/status | 更新用户状态 |

#### 文件接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/files/upload | 上传文件 |
| POST | /api/files/upload/batch | 批量上传 |
| GET | /api/files/page | 分页查询文件 |
| GET | /api/files/{id} | 获取文件信息 |
| GET | /api/files/download/{id} | 下载文件 |
| DELETE | /api/files/{id} | 删除文件 |

---

## 常见问题

### Q1: Token验证失败

**现象**：`Token已失效或不存在于Redis中`

**原因**：
1. Redis服务未启动
2. Token过期
3. Token与Redis中存储的不一致

**解决方案**：
```bash
# 检查Redis服务
redis-cli ping
# 应返回 PONG

# 重启Redis
redis-server

# 重新登录获取新Token
```

### Q2: 数据库连接失败

**现象**：`Communications link failure`

**解决方案**：
```yaml
# 检查配置
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/template_db?useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: root
```

### Q3: 文件上传失败

**现象**：`文件大小超过限制`

**解决方案**：
```yaml
spring:
  servlet:
    multipart:
      max-file-size: 50MB      # 调整单文件大小限制
      max-request-size: 100MB  # 调整请求大小限制
```

### Q4: 跨域问题

**解决方案**：

已在 `SecurityConfig.java` 中配置CORS：

```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(List.of("*"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setAllowCredentials(true);
    return source;
}
```

### Q5: JWT密钥警告

**现象**：生产环境使用默认密钥

**解决方案**：
```yaml
jwt:
  secret: your-very-long-secret-key-at-least-256-bits
```

---

## 贡献指南

### 开发环境搭建

```bash
# 1. Fork项目到个人仓库

# 2. Clone到本地
git clone https://github.com/Lzzzzzzj/java-web-backend-template.git

# 3. 创建开发分支
git checkout -b feature/your-feature-name

# 4. 安装依赖
./mvnw clean install

# 5. 启动开发环境
./mvnw spring-boot:run
```

### 代码规范

#### 命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| 类名 | 大驼峰 | `UserService` |
| 方法名 | 小驼峰 | `getUserById` |
| 常量 | 全大写下划线 | `MAX_SIZE` |
| 包名 | 全小写 | `com.example.back` |

#### 注释规范

```java
/**
 * 用户服务接口
 * 提供用户相关的业务操作
 *
 * @author your-name
 * @since 1.0.0
 */
public interface UserService {

    /**
     * 用户登录
     *
     * @param loginDTO 登录信息
     * @return Token信息
     * @throws BusinessException 用户不存在或密码错误
     */
    TokenVO login(LoginDTO loginDTO);
}
```

#### 提交规范

```bash
# 提交格式
<type>(<scope>): <subject>

# 类型说明
feat:     新功能
fix:      修复bug
docs:     文档更新
style:    代码格式调整
refactor: 重构
test:     测试相关
chore:    构建/工具相关

# 示例
feat(user): 添加用户头像上传功能
fix(auth): 修复Token过期时间计算错误
docs(readme): 更新部署文档
```

### Pull Request流程

1. 确保代码通过所有测试
2. 更新相关文档
3. 提交PR并填写PR模板
4. 等待Code Review
5. 根据反馈修改代码
6. 合并到主分支

### PR模板

```markdown
## 变更类型
- [ ] 新功能
- [ ] Bug修复
- [ ] 文档更新
- [ ] 代码重构

## 变更说明
简要描述本次变更的内容

## 关联Issue
Closes #issue-number

## 测试说明
- [ ] 已添加单元测试
- [ ] 已通过集成测试
- [ ] 已手动测试

## 检查清单
- [ ] 代码符合项目规范
- [ ] 已更新相关文档
- [ ] 无新增警告
```

---

## 许可证

本项目采用 [Apache License 2.0](LICENSE) 许可证。

---

## 联系方式

- GitHub: [https://github.com/Lzzzzzzj](https://github.com/Lzzzzzzj)
- Repository: [https://github.com/Lzzzzzzj/java-web-backend-template](https://github.com/Lzzzzzzj/java-web-backend-template)
- Issue: [GitHub Issues](https://github.com/Lzzzzzzj/java-web-backend-template/issues)

---

**感谢使用本项目，如有问题欢迎提Issue或PR！**
