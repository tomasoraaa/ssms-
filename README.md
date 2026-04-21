# 学生成绩管理系统 (SSMS)

一个基于 Spring Boot + Vue 的学生成绩管理系统，支持学生、教师、管理员三种角色，提供完整的选课、成绩管理、课程评价等功能。

## 项目结构

```
ssms/
├── springboot/          # 后端 Spring Boot 项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/lgs/
│   │   │   │   ├── controller/   # 控制器层
│   │   │   │   ├── service/     # 服务层
│   │   │   │   ├── mapper/      # 数据访问层
│   │   │   │   └── entity/       # 实体类
│   │   │   └── resources/
│   │   │       ├── mapper/      # MyBatis XML 映射文件
│   │   │       └── application.yml  # 应用配置
│   │   └── test/
│   └── pom.xml
├── vue/                 # 前端 Vue 项目
│   ├── src/
│   │   ├── views/       # 页面组件
│   │   │   ├── front/   # 前台（学生/教师）
│   │   │   └── manager/ # 后台（管理员）
│   │   ├── utils/       # 工具函数
│   │   └── router/      # 路由配置
│   └── package.json
└── sql/                 # 数据库脚本
    └── ssms.sql
```

## 技术栈

### 后端
- **Spring Boot 3.3.1** - Web 框架
- **MyBatis** - ORM 框架
- **MySQL 8.0** - 数据库
- **PageHelper** - 分页插件
- **JWT** - 身份认证

### 前端
- **Vue 3** - 渐进式 JavaScript 框架
- **Element Plus** - UI 组件库
- **ECharts** - 数据可视化
- **Axios** - HTTP 请求库
- **Vite** - 构建工具

## 环境要求

- **JDK 21** 或更高版本
- **Node.js 16** 或更高版本
- **MySQL 8.0** 或更高版本
- **Maven 3.6** 或更高版本

## 安装与运行

### 1. 数据库配置

1. 确保 MySQL 服务已启动
2. 创建数据库 `sms`：
```sql
CREATE DATABASE sms DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
3. 导入数据库脚本：
```bash
mysql -u root -p sms < sql/ssms.sql
```

### 2. 修改数据库配置

编辑 `springboot/src/main/resources/application.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    username: root
    password: 你的数据库密码
    url: jdbc:mysql://localhost:3306/sms?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=false&serverTimezone=GMT%2b8&allowPublicKeyRetrieval=true
```

### 3. 启动后端服务

```bash
cd ssms/springboot
mvn clean package
java -jar target/springboot-0.0.1-SNAPSHOT.jar
```

后端服务启动后运行在 `http://localhost:9090`

### 4. 启动前端服务

```bash
cd ssms/vue
npm install
npm run dev
```

前端服务启动后访问 `http://localhost:5173` 或 `http://localhost:5174`

## 默认账号

| 角色 | 账号 | 密码 |
|------|------|------|
| 管理员 | admin | 123456 |
| 教师 | T2024001 | 123456789ok |
| 教师 | T2024002 | 123456 |
| 学生 | 20240101001 | 123456 |
| 学生 | 20240101002 | 123456 |

## 功能模块

### 管理员功能
- **用户管理**：学生、教师账号的增删改查和批量导入
- **教学管理**：
  - 学年学期管理（设置每学期的选课时间）
  - 行政班管理（年级、专业、班级划分）
  - 教学班管理（课程安排、教师分配）
- **成绩管理**：学生成绩录入与修改
- **课程评价**：查看学生对课程的评价
- **系统设置**：选课功能开关、教师录入成绩权限控制
- **数据统计**：学生、教师、课程数量概览

### 教师功能
- **我的课程**：教授课程列表
- **成绩管理**：录入和修改学生成绩
- **课程评价**：查看和回复学生评价
- **个人信息**：修改个人资料和密码

### 学生功能
- **我的课程**：已选课程列表和成绩查看
- **成绩分析**：成绩分布图表和 GPA 统计
- **选课管理**：查看可选课程、提交选课申请
- **退课管理**：提交退课申请
- **课程评价**：对已修课程进行评价
- **个人信息**：修改个人资料和密码

## 数据库表说明

| 表名 | 说明 |
|------|------|
| admin | 管理员信息 |
| student | 学生信息 |
| teacher | 教师信息 |
| course | 课程信息 |
| admin_class | 行政班表 |
| teaching_class | 教学班表 |
| course_teacher | 课程教师关联表 |
| academic_year | 学年学期表 |
| student_admin_class | 学生行政班关联表 |
| student_course | 学生选课成绩表 |
| course_selection | 选课申请表 |
| course_withdrawal | 退课申请表 |
| course_evaluation | 课程评价表 |
| modify_request | 信息修改申请表 |
| activity_log | 操作日志表 |
| system_config | 系统配置表 |

## API 接口说明

后端 API 接口统一前缀为 `/api` 或对应模块路径，主要包括：

- `/admin/*` - 管理员相关接口
- `/student/*` - 学生相关接口
- `/teacher/*` - 教师相关接口
- `/course/*` - 课程相关接口
- `/courseSelection/*` - 选课相关接口
- `/academicYear/*` - 学年学期相关接口
- `/adminClass/*` - 行政班相关接口
- `/teachingClass/*` - 教学班相关接口
- `/courseEvaluation/*` - 课程评价相关接口

## 常见问题

### 1. 数据库连接失败
- 检查 MySQL 服务是否启动
- 确认数据库用户名和密码正确
- 确认数据库 `sms` 已创建

### 2. 前端无法访问后端接口
- 确认后端服务已启动在 9090 端口
- 检查 `vue/src/utils/request.js` 中的 baseURL 配置

### 3. 跨域问题
后端已配置 CORS 跨域支持，如仍有问题请检查浏览器控制台。

### 4. 成绩分析页面不显示学年
确保 `student_course` 表中的 `academic_year_id` 字段有值。

## 开发说明

### 命名规范
- 数据库字段：下划线命名（snake_case）
- Java 实体类：下划线命名，对应 getter/setter 使用驼峰
- 前端变量：驼峰命名

### 主要修改记录
- 2024-2025学年第一学期数据已初始化
- 行政班：CS2024-1、CS2024-2、SE2024-1 等已创建
- 部分学生已分配行政班

## 许可证

本项目仅供学习交流使用。