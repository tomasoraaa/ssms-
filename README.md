# 学生成绩管理系统 (SSMS)

一个基于 **Spring Boot 3.x + Vue 3.x** 的现代化学生成绩管理系统，支持学生、教师、管理员三种角色，提供完整的选课管理、成绩管理、课程评价、补考管理等功能。

---

## 一、项目背景与意义

### 1.1 项目背景

随着高校规模的不断扩大，学生数量和课程种类日益增多，传统的成绩管理方式已难以满足现代教育管理的需求。本系统旨在通过信息化手段，实现学生成绩管理的自动化、规范化和智能化，提高教学管理效率，为师生提供便捷的成绩查询和管理服务。

### 1.2 研究意义

- **提高管理效率**：自动化成绩录入、统计和分析，减轻教师和管理人员的工作负担
- **提升服务质量**：学生可以随时查询成绩，了解学习情况
- **数据安全可靠**：集中存储成绩数据，避免数据丢失和篡改
- **决策支持**：提供成绩分析和统计功能，为教学管理决策提供数据支持

---

## 二、功能亮点

| 功能 | 描述 |
|------|------|
| **三端架构** | 学生端、教师端、管理端，角色权限分明 |
| **补考区分** | 成绩展示中明确区分正常/补考/缓考成绩，显示原始不及格成绩 |
| **自动选课** | 学生提交选课申请后系统自动校验并完成选课 |
| **时间冲突检测** | 选课时自动检测已选课程时间是否冲突 |
| **教学班管理** | 完整的教学班创建、编辑、学生管理功能 |
| **成绩导出** | 支持导出成绩为Excel文件（三端均支持） |
| **课程评价** | 学生对已修课程进行评价，教师可查看和回复 |
| **系统配置** | 支持选课功能开关、成绩录入权限控制等 |

---

## 三、技术栈

### 3.1 后端技术

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.3.1 | Web 应用框架 |
| Spring Security | 6.x | 安全框架（JWT认证） |
| MyBatis | 3.0.3 | ORM 框架 |
| MySQL | 8.0+ | 关系型数据库 |
| PageHelper | 6.4.0 | 分页插件 |
| Apache POI | 5.2.x | Excel 文件处理 |

### 3.2 前端技术

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.4.x | 渐进式 JavaScript 框架 |
| Element Plus | 2.8.x | UI 组件库 |
| ECharts | 5.6.x | 数据可视化图表 |
| Axios | 1.7.x | HTTP 请求库 |
| Vite | 5.4.x | 构建工具 |
| Vue Router | 4.4.x | 路由管理 |

---

## 四、项目结构

```
毕业设计-学生成绩管理系统/
├── ssms/
│   ├── springboot/                    # 后端 Spring Boot 项目
│   │   ├── src/main/java/com/lgs/
│   │   │   ├── controller/           # REST API 控制器
│   │   │   │   ├── AdminController.java
│   │   │   │   ├── StudentController.java
│   │   │   │   ├── TeacherController.java
│   │   │   │   └── ScoreExportController.java  # 成绩导出
│   │   │   ├── service/              # 业务逻辑层
│   │   │   │   ├── impl/             # 服务实现类
│   │   │   │   └── *Service.java     # 服务接口
│   │   │   ├── mapper/               # MyBatis 数据访问层
│   │   │   ├── entity/               # 数据库实体类
│   │   │   ├── config/               # 配置类（CORS、Swagger等）
│   │   │   ├── interceptor/          # 拦截器（JWT校验）
│   │   │   └── exception/            # 异常处理
│   │   ├── src/main/resources/
│   │   │   ├── mapper/               # MyBatis XML 映射文件
│   │   │   └── application.yml       # 应用配置
│   │   └── pom.xml                   # Maven 依赖
│   └── vue/                          # 前端 Vue 项目
│       ├── src/
│       │   ├── views/                # 页面组件
│       │   │   ├── front/            # 前台（学生/教师）
│       │   │   │   ├── student/      # 学生端页面
│       │   │   │   └── teacher/      # 教师端页面
│       │   │   └── manager/          # 后台（管理员）
│       │   ├── utils/                # 工具函数
│       │   │   └── request.js        # Axios 封装
│       │   ├── router/               # 路由配置
│       │   └── style/                # 全局样式
│       └── package.json              # 前端依赖
├── postman/                          # Postman API 测试集合
├── files/                            # 上传文件存储目录
└── sql/                              # 数据库脚本
    ├── ssms.sql                      # 初始数据库脚本
    └── add_makeup_flag_fields.sql    # 补考字段添加脚本
```

---

## 五、环境要求

| 环境 | 版本 | 说明 |
|------|------|------|
| JDK | 21+ | Java 开发环境 |
| Node.js | 18+ | JavaScript 运行环境 |
| MySQL | 8.0+ | 数据库管理系统 |
| Maven | 3.6+ | 项目构建工具 |

---

## 六、安装与运行

### 6.1 数据库配置

```bash
# 1. 创建数据库
mysql -u root -p -e "CREATE DATABASE sms DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 2. 导入初始数据
mysql -u root -p sms < sql/ssms.sql

# 3. 执行补考字段更新脚本（如需补考功能）
mysql -u root -p sms < sql/add_makeup_flag_fields.sql
```

### 6.2 修改数据库配置

编辑 `ssms/springboot/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    username: root                    # 数据库用户名
    password: your_password           # 数据库密码
    url: jdbc:mysql://localhost:3306/sms?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=false&serverTimezone=GMT%2b8&allowPublicKeyRetrieval=true
```

### 6.3 启动后端服务

```bash
cd ssms/springboot
mvn clean package -DskipTests
java -jar target/springboot-0.0.1-SNAPSHOT.jar
```

后端服务运行在 `http://localhost:9090`

### 6.4 启动前端服务

```bash
cd ssms/vue
npm install
npm run dev
```

前端服务启动后访问 `http://localhost:5173`

---

## 七、默认账号

| 角色 | 账号 | 密码 | 说明 |
|------|------|------|------|
| 管理员 | admin | 123456 | 系统管理员 |
| 教师 | T2024001 | 123456789ok | 教师账号1 |
| 教师 | T2024002 | 123456 | 教师账号2 |
| 学生 | 20240101001 | 123456 | 学生账号1 |
| 学生 | 20240101002 | 123456 | 学生账号2 |

---

## 八、功能模块

### 8.1 管理员功能

| 模块 | 功能 | 描述 |
|------|------|------|
| 用户管理 | 学生管理 | 学生账号的增删改查、批量导入 |
| | 教师管理 | 教师账号的增删改查、批量导入 |
| 教学管理 | 学年学期管理 | 设置每学期的选课时间和状态 |
| | 行政班管理 | 年级、专业、班级的划分和管理 |
| | 教学班管理 | 课程安排、教师分配、学生管理、成绩查看与导出 |
| 成绩管理 | 成绩查看 | 查看各教学班成绩，区分补考/缓考成绩 |
| | 成绩导出 | 导出教学班成绩为Excel文件 |
| 课程评价 | 评价查看 | 查看学生对课程的评价 |
| 系统设置 | 功能开关 | 选课功能开关、成绩录入权限控制 |
| | 数据统计 | 学生、教师、课程数量概览 |

### 8.2 教师功能

| 模块 | 功能 | 描述 |
|------|------|------|
| 我的课程 | 课程列表 | 教授课程列表（含教学班详情展示） |
| 成绩管理 | 成绩录入 | 录入和修改学生成绩，支持补考成绩录入 |
| | 批量导入 | 通过Excel文件批量导入成绩 |
| 成绩分析 | 统计分析 | 成绩分布图表、平均分统计，显示补考区分 |
| | 成绩导出 | 在成绩分析页面导出教学班成绩 |
| 课程评价 | 评价查看 | 查看学生对所授课程的评价 |
| | 评价回复 | 回复学生评价 |
| 个人信息 | 资料修改 | 修改个人资料和密码 |

### 8.3 学生功能

| 模块 | 功能 | 描述 |
|------|------|------|
| 我的课程 | 课程列表 | 已选课程列表和成绩查看（含补考区分） |
| 成绩分析 | 成绩统计 | 成绩分布图表和 GPA 统计 |
| | 成绩导出 | 导出个人成绩为Excel文件 |
| 选课管理 | 选课申请 | 查看可选课程、提交选课申请 |
| | 退课申请 | 提交退课申请 |
| 课程评价 | 课程评价 | 对已修课程进行评价 |
| 个人信息 | 资料修改 | 修改个人资料和密码 |

---

## 九、核心功能详细设计

### 9.1 补考区分功能

#### 9.1.1 数据库设计

| 字段名 | 类型 | 说明 |
|--------|------|------|
| `is_makeup` | TINYINT(1) | 是否补考：0-否，1-是 |
| `original_score` | DECIMAL(5,2) | 原始不及格成绩（仅补考记录） |
| `makeup_exam_type` | VARCHAR(20) | 补考类型：`补考`/`缓考` |

#### 9.1.2 显示效果

| 课程名称 | 总评成绩 | 成绩来源 |
|----------|----------|----------|
| 数据结构 | 85.0 | 🟢 正常 |
| 操作系统 | 60.0 (原: 49.0) | 🟠 补考 |
| 计算机网络 | 75.0 | 🟠 缓考 |

### 9.2 成绩导出功能

#### 9.2.1 导出内容

| 字段 | 说明 |
|------|------|
| 学号 | 学生学号 |
| 姓名 | 学生姓名 |
| 平时成绩 | 平时成绩（占比30%） |
| 期中成绩 | 期中成绩（占比20%） |
| 期末成绩 | 期末成绩（占比50%） |
| 总评成绩 | 加权计算后的总评成绩 |
| 成绩来源 | 正常/补考/缓考 |
| 原始成绩 | 补考的原始不及格成绩 |
| 绩点 | GPA绩点 |
| 等级 | 优秀/良好/中等/及格/不及格 |

#### 9.2.2 API 接口

| 接口 | 路径 | HTTP方法 | 参数 |
|------|------|----------|------|
| 教师导出 | `/scoreExport/teacher` | GET | teaching_class_id |
| 学生导出 | `/scoreExport/student` | GET | student_id |
| 管理员导出 | `/scoreExport/admin` | GET | teaching_class_id |

### 9.3 选课与时间冲突检测

#### 9.3.1 功能描述

- 学生提交选课申请时，系统自动检测时间冲突
- 检测逻辑：比对已选课程与待选课程的上课时间
- 如存在时间冲突，提示学生并阻止选课

#### 9.3.2 检测规则

| 检测项 | 说明 |
|--------|------|
| 日期相同 | 同一日期 |
| 时间段重叠 | 上课时间有重叠 |
| 周次相同 | 同一周次内 |

---

## 十、数据库表结构

### 10.1 核心表说明

| 表名 | 说明 | 关键字段 |
|------|------|----------|
| `admin` | 管理员信息 | id, username, password |
| `student` | 学生信息 | id, username, name, profession |
| `teacher` | 教师信息 | id, username, name, phone |
| `course` | 课程信息 | id, course_code, course_name, credit |
| `teaching_class` | 教学班表 | id, course_id, teacher_id, class_code, max_students |
| `student_course` | 学生选课成绩表 | id, student_id, course_id, score |
| `score_detail` | 成绩详情表 | id, student_id, teaching_class_id, usual_score, midterm_score, final_score, total_score, is_makeup, original_score, makeup_exam_type |
| `course_selection` | 选课申请表 | id, student_id, course_id, status |
| `course_evaluation` | 课程评价表 | id, student_id, course_id, score, comment |

### 10.2 成绩详情表结构

```sql
CREATE TABLE score_detail (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id VARCHAR(50) NOT NULL,
    teaching_class_id BIGINT NOT NULL,
    usual_score DECIMAL(5,2) DEFAULT 0,
    midterm_score DECIMAL(5,2) DEFAULT 0,
    final_score DECIMAL(5,2) DEFAULT 0,
    total_score DECIMAL(5,2) DEFAULT 0,
    is_makeup TINYINT(1) DEFAULT 0 COMMENT '是否补考：0-否，1-是',
    original_score DECIMAL(5,2) COMMENT '原始不及格成绩',
    makeup_exam_type VARCHAR(20) COMMENT '补考类型：补考/缓考',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_student_id (student_id),
    INDEX idx_teaching_class_id (teaching_class_id)
);
```

---

## 十一、API 接口说明

### 11.1 认证接口

| 接口 | 路径 | 说明 |
|------|------|------|
| 管理员登录 | `/admin/login` | POST |
| 学生登录 | `/student/login` | POST |
| 教师登录 | `/teacher/login` | POST |

### 11.2 核心业务接口

| 模块 | 路径 | 说明 |
|------|------|------|
| 管理员 | `/admin/*` | 管理员相关接口 |
| 学生 | `/student/*` | 学生相关接口 |
| 教师 | `/teacher/*` | 教师相关接口 |
| 课程 | `/course/*` | 课程相关接口 |
| 教学班 | `/teachingClass/*` | 教学班管理 |
| 成绩详情 | `/scoreDetail/*` | 成绩详情（含补考） |
| 选课 | `/courseSelection/*` | 选课申请 |
| 成绩导出 | `/scoreExport/*` | Excel导出 |

---

## 十二、系统流程图

### 12.1 成绩录入流程

```
教师登录 → 选择课程 → 选择教学班 → 查看学生列表 → 录入成绩 → 保存成绩
                                                      ↓
                                               计算总评成绩
                                                      ↓
                                               检测是否补考
                                                      ↓
                                               设置补考标识和原始成绩
```

### 12.2 选课流程

```
学生登录 → 查看可选课程 → 选择课程 → 检测时间冲突 → 提交选课申请
                                           ↓
                                    时间冲突？→ 是 → 提示冲突信息
                                           ↓ 否
                                    检查教学班容量 → 已满？→ 是 → 提示容量已满
                                           ↓ 否
                                    完成选课
```

---

## 十三、部署说明

### 13.1 开发环境

- **后端端口**：9090
- **前端端口**：5173
- **数据库**：MySQL 8.0+

### 13.2 生产环境部署

```bash
# 前端构建
cd ssms/vue
npm run build

# 后端打包
cd ssms/springboot
mvn clean package -DskipTests

# 运行
java -jar target/springboot-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

---

## 十四、常见问题

### 14.1 数据库连接失败
- 检查 MySQL 服务是否启动
- 确认数据库用户名和密码正确
- 确认数据库 `sms` 已创建

### 14.2 前端无法访问后端接口
- 确认后端服务已启动在 9090 端口
- 检查 `vue/src/utils/request.js` 中的 baseURL 配置

### 14.3 跨域问题
- 后端已配置 CORS 跨域支持
- 检查浏览器控制台的具体错误信息

### 14.4 补考成绩不显示
- 确认 `score_detail` 表已添加补考字段
- 确认后端逻辑正确处理补考数据

### 14.5 选课时间冲突
- 检查课程时间设置是否正确
- 确认教学班上课时间字段已正确配置

---

## 十五、开发规范

### 15.1 命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| 数据库字段 | 下划线命名 | `student_id`, `course_code` |
| Java 类 | 驼峰命名 | `ScoreDetailServiceImpl` |
| Java 变量 | 驼峰命名 | `isMakeup`, `originalScore` |
| 前端变量 | 驼峰命名 | `selectedClassId`, `studentList` |
| 接口路径 | 小写加下划线 | `/score_export/teacher` |

### 15.2 代码规范

- **Java**：遵循阿里巴巴 Java 开发手册
- **Vue**：遵循 Vue 官方风格指南
- **SQL**：关键字大写，字段名小写

---

## 十六、修改记录

### 16.1 新增功能：补考区分
- **feat**: 在 `score_detail` 表添加补考标识字段
- **feat**: 修改 `ScoreDetail` 实体类添加补考相关属性
- **feat**: 修改 `ScoreDetailServiceImpl`，检测补考记录并保存原始成绩
- **feat**: 更新三端成绩展示页面，显示补考区分和原始成绩

### 16.2 新增功能：成绩导出
- **feat**: 创建 `ScoreExportController` 控制器
- **feat**: 实现教师端、学生端、管理端成绩导出
- **feat**: 使用 Apache POI 生成 Excel 文件

### 16.3 新增功能：自动选课与时间冲突检测
- **feat**: 实现自动选课功能
- **feat**: 新增时间冲突检测机制

---

## 十七、致谢

本项目是基于 Spring Boot 和 Vue 3 的学习实践项目，参考了多个优秀的开源项目和技术文档。感谢所有为开源社区做出贡献的开发者。

---

## 十八、许可证

本项目仅供学习交流和毕业设计使用。