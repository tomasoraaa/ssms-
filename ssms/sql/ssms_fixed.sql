/*
学生成绩管理系统 - 数据库初始化脚本
字符集: utf8mb4
排序规则: utf8mb4_unicode_ci
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for academic_year
-- ----------------------------
DROP TABLE IF EXISTS `academic_year`;
CREATE TABLE `academic_year` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `year` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '学年，如 2024-2025',
  `semester` int NOT NULL COMMENT '学期：1-第一学期，2-第二学期',
  `start_date` date NULL DEFAULT NULL COMMENT '学期开始日期',
  `end_date` date NULL DEFAULT NULL COMMENT '学期结束日期',
  `selection_start` datetime NULL DEFAULT NULL COMMENT '选课开始时间',
  `selection_end` datetime NULL DEFAULT NULL COMMENT '选课结束时间',
  `status` int NULL DEFAULT 1 COMMENT '状态：0-未开启，1-进行中，2-已结束',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_year_semester`(`year` ASC, `semester` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '学年学期表' ROW_FORMAT = Dynamic;

INSERT INTO `academic_year` VALUES (1, '2024-2025', 1, '2024-09-01', '2025-01-15', '2024-08-01 00:00:00', '2024-08-31 23:59:59', 2);
INSERT INTO `academic_year` VALUES (2, '2024-2025', 2, '2025-02-20', '2025-07-15', '2025-02-01 00:00:00', '2025-02-28 23:59:59', 0);

-- ----------------------------
-- Table structure for activity_log
-- ----------------------------
DROP TABLE IF EXISTS `activity_log`;
CREATE TABLE `activity_log` (
  `id` int NOT NULL AUTO_INCREMENT,
  `operate_time` datetime NOT NULL,
  `operate_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `operate_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_operate_time`(`operate_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 180 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

INSERT INTO `activity_log` VALUES (1, '2026-04-19 17:06:31', '登录', '20240101001', '学生 20240101001 登录系统', 'STUDENT');
INSERT INTO `activity_log` VALUES (2, '2026-04-19 18:54:46', '登录', 'T2024001', '教师 T2024001 登录系统', 'TEACHER');
INSERT INTO `activity_log` VALUES (3, '2026-04-19 19:16:16', '用户', 'admin', '修改管理员 admin 的信息', 'ADMIN');
INSERT INTO `activity_log` VALUES (4, '2026-04-19 19:33:35', '登录', 'T2024002', '教师 T2024002 登录系统', 'TEACHER');
INSERT INTO `activity_log` VALUES (5, '2026-04-19 19:33:54', '登录', 'T2024001', '教师 T2024001 登录系统', 'TEACHER');
INSERT INTO `activity_log` VALUES (6, '2026-04-19 19:36:14', '登录', 'T2024001', '教师 T2024001 登录系统', 'TEACHER');
INSERT INTO `activity_log` VALUES (7, '2026-04-19 19:36:43', '选课', '20240101001', '学生 20240101001 申请选课', 'STUDENT');
INSERT INTO `activity_log` VALUES (8, '2026-04-19 19:37:12', '选课', '20240101001', '学生 20240101001 申请选课', 'STUDENT');
INSERT INTO `activity_log` VALUES (9, '2026-04-19 19:37:20', '选课', '20240101001', '学生 20240101001 申请选课', 'STUDENT');
INSERT INTO `activity_log` VALUES (10, '2026-04-19 19:37:25', '选课', '20240101001', '学生 20240101001 申请选课', 'STUDENT');
INSERT INTO `activity_log` VALUES (11, '2026-04-19 19:38:28', '选课', 'admin', '批准选课申请', 'ADMIN');
INSERT INTO `activity_log` VALUES (12, '2026-04-19 19:38:28', '选课', 'admin', '批准选课申请', 'ADMIN');
INSERT INTO `activity_log` VALUES (13, '2026-04-19 19:38:29', '选课', 'admin', '批准选课申请', 'ADMIN');
INSERT INTO `activity_log` VALUES (14, '2026-04-19 19:38:29', '选课', 'admin', '批准选课申请', 'ADMIN');

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员信息' ROW_FORMAT = DYNAMIC;

INSERT INTO `admin` VALUES (1, 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', '管理员', NULL, 'ADMIN');

-- ----------------------------
-- Table structure for admin_class
-- ----------------------------
DROP TABLE IF EXISTS `admin_class`;
CREATE TABLE `admin_class` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `grade` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '年级，如 2024',
  `major` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '专业',
  `class_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '班级名称，如 计算机1班',
  `class_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '班级代码，如 CS2024-1',
  `counselor_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '辅导员/班主任工号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_class_code`(`class_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '行政班表' ROW_FORMAT = Dynamic;

INSERT INTO `admin_class` VALUES (1, '2024', '计算机科学与技术', '计算机1班', 'CS2024-1', 'T2024001');
INSERT INTO `admin_class` VALUES (2, '2024', '计算机科学与技术', '计算机2班', 'CS2024-2', 'T2024002');
INSERT INTO `admin_class` VALUES (3, '2024', '软件工程', '软件工程1班', 'SE2024-1', 'T2024003');
INSERT INTO `admin_class` VALUES (4, '2024', '软件工程', '软件工程2班', 'SE2024-2', 'T2024004');
INSERT INTO `admin_class` VALUES (5, '2023', '计算机科学与技术', '计算机1班', 'CS2023-1', 'T2024005');
INSERT INTO `admin_class` VALUES (6, '2023', '计算机科学与技术', '计算机2班', 'CS2023-2', 'T2024006');
INSERT INTO `admin_class` VALUES (7, '2023', '软件工程', '软件工程1班', 'SE2023-1', 'T2024007');
INSERT INTO `admin_class` VALUES (8, '2023', '软件工程', '软件工程2班', 'SE2023-2', 'T2024008');

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `id` int NOT NULL AUTO_INCREMENT,
  `course_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '课程名称',
  `course_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '课程代码',
  `credit` int NULL DEFAULT NULL COMMENT '学分',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '课程描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

INSERT INTO `course` VALUES (1, '高等数学', 'MATH101', 4, '高等数学基础课程');
INSERT INTO `course` VALUES (2, '线性代数', 'MATH102', 3, '线性代数基础');
INSERT INTO `course` VALUES (3, '计算机组成原理', 'CS101', 4, '计算机硬件基础');
INSERT INTO `course` VALUES (4, '数据结构', 'CS102', 4, '数据结构与算法');
INSERT INTO `course` VALUES (5, '操作系统', 'CS103', 4, '操作系统原理');
INSERT INTO `course` VALUES (6, '数据库系统', 'CS104', 4, '数据库原理与应用');
INSERT INTO `course` VALUES (7, '软件工程', 'CS105', 3, '软件工程方法学');
INSERT INTO `course` VALUES (8, '计算机网络', 'CS106', 4, '计算机网络基础');
INSERT INTO `course` VALUES (9, 'Java程序设计', 'CS107', 3, 'Java语言编程');
INSERT INTO `course` VALUES (10, 'Python编程', 'CS108', 3, 'Python语言编程');
INSERT INTO `course` VALUES (11, '人工智能', 'CS201', 3, '人工智能基础');
INSERT INTO `course` VALUES (12, '机器学习', 'CS202', 3, '机器学习导论');
INSERT INTO `course` VALUES (13, '深度学习', 'CS203', 3, '深度学习技术');
INSERT INTO `course` VALUES (14, '软件工程实践', 'CS204', 2, '软件工程实践课程');
INSERT INTO `course` VALUES (15, '毕业设计', 'CS205', 6, '本科毕业设计');

-- ----------------------------
-- Table structure for course_evaluation
-- ----------------------------
DROP TABLE IF EXISTS `course_evaluation`;
CREATE TABLE `course_evaluation` (
  `id` int NOT NULL AUTO_INCREMENT,
  `teaching_class_id` int NULL DEFAULT NULL COMMENT '教学班ID',
  `student_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '学生ID',
  `score` int NULL DEFAULT NULL COMMENT '评分(1-5)',
  `comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '评价内容',
  `evaluation_time` datetime NULL DEFAULT NULL COMMENT '评价时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for course_selection
-- ----------------------------
DROP TABLE IF EXISTS `course_selection`;
CREATE TABLE `course_selection` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '学生ID',
  `teaching_class_id` int NULL DEFAULT NULL COMMENT '教学班ID',
  `status` int NULL DEFAULT 0 COMMENT '状态：0-待审核，1-已批准，2-已拒绝',
  `apply_time` datetime NULL DEFAULT NULL COMMENT '申请时间',
  `approval_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for course_teacher
-- ----------------------------
DROP TABLE IF EXISTS `course_teacher`;
CREATE TABLE `course_teacher` (
  `id` int NOT NULL AUTO_INCREMENT,
  `course_id` int NULL DEFAULT NULL COMMENT '课程ID',
  `teacher_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '教师ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for course_withdrawal
-- ----------------------------
DROP TABLE IF EXISTS `course_withdrawal`;
CREATE TABLE `course_withdrawal` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '学生ID',
  `teaching_class_id` int NULL DEFAULT NULL COMMENT '教学班ID',
  `status` int NULL DEFAULT 0 COMMENT '状态：0-待审核，1-已批准，2-已拒绝',
  `apply_time` datetime NULL DEFAULT NULL COMMENT '申请时间',
  `approval_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for makeup_exam
-- ----------------------------
DROP TABLE IF EXISTS `makeup_exam`;
CREATE TABLE `makeup_exam` (
  `id` int NOT NULL AUTO_INCREMENT,
  `student_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '学生ID',
  `teaching_class_id` int NULL DEFAULT NULL COMMENT '教学班ID',
  `course_id` int NULL DEFAULT NULL COMMENT '课程ID',
  `original_score` double NULL DEFAULT NULL COMMENT '原成绩',
  `makeup_score` double NULL DEFAULT NULL COMMENT '补考成绩',
  `status` int NULL DEFAULT 0 COMMENT '状态：0-待审核，1-已批准，2-已拒绝，3-已完成',
  `apply_time` datetime NULL DEFAULT NULL COMMENT '申请时间',
  `exam_time` datetime NULL DEFAULT NULL COMMENT '考试时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for modify_request
-- ----------------------------
DROP TABLE IF EXISTS `modify_request`;
CREATE TABLE `modify_request` (
  `id` int NOT NULL AUTO_INCREMENT,
  `score_detail_id` int NULL DEFAULT NULL COMMENT '成绩详情ID',
  `student_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '学生ID',
  `reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改原因',
  `status` int NULL DEFAULT 0 COMMENT '状态：0-待审核，1-已批准，2-已拒绝',
  `apply_time` datetime NULL DEFAULT NULL COMMENT '申请时间',
  `approval_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for score_detail
-- ----------------------------
DROP TABLE IF EXISTS `score_detail`;
CREATE TABLE `score_detail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `student_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '学生ID',
  `course_id` int NULL DEFAULT NULL COMMENT '课程ID',
  `teaching_class_id` int NULL DEFAULT NULL COMMENT '教学班ID',
  `usual_score` double NULL DEFAULT NULL COMMENT '平时成绩',
  `midterm_score` double NULL DEFAULT NULL COMMENT '期中成绩',
  `final_score` double NULL DEFAULT NULL COMMENT '期末成绩',
  `total_score` double NULL DEFAULT NULL COMMENT '总成绩',
  `academic_year_id` int NULL DEFAULT NULL COMMENT '学年ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for score_rule
-- ----------------------------
DROP TABLE IF EXISTS `score_rule`;
CREATE TABLE `score_rule` (
  `id` int NOT NULL AUTO_INCREMENT,
  `rule_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '规则名称',
  `usual_weight` int NULL DEFAULT 30 COMMENT '平时成绩权重',
  `midterm_weight` int NULL DEFAULT 20 COMMENT '期中成绩权重',
  `final_weight` int NULL DEFAULT 50 COMMENT '期末成绩权重',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '规则描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

INSERT INTO `score_rule` VALUES (1, '默认评分规则', 30, 20, 50, '平时30% + 期中20% + 期末50%');

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `gender` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '性别',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色',
  `status` int NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `class_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '班级代码',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '学生信息' ROW_FORMAT = DYNAMIC;

INSERT INTO `student` VALUES (1, '20240101001', '$2a$10$eeiPULc3FYqamJR5JuUOYecvqONoexJWHkXz/BjYF249rZJuV.qdi', '张三', NULL, '男', '13912340001', 'STUDENT', 1, 'zhangsan@cs.edu.cn', 'CS2024-1');
INSERT INTO `student` VALUES (2, '20240101002', '$2a$10$kpgxjUQFFOnog/E7q/xZq.E0F0UkhACpQnz2GNShQ/0QET8stCqfO', '李四', NULL, '女', '13912340002', 'STUDENT', 1, 'lisi@cs.edu.cn', 'CS2024-1');
INSERT INTO `student` VALUES (3, '20240101003', '$2a$10$9c7DOS0BZFJHzc093XUj1eqcZkgLjurt7OkBa6ReSL84rLt6U1ygm', '王五', NULL, '男', '13912340003', 'STUDENT', 1, 'wangwu@cs.edu.cn', 'CS2024-1');
INSERT INTO `student` VALUES (4, '20240101004', '$2a$10$IBxpV8MZLQhkzdfadbkSWu.lwwIPZzIGpQG5C4Iom8XESAamrvUEq', '赵六', NULL, '女', '13912340004', 'STUDENT', 1, 'zhaoliu@cs.edu.cn', 'CS2024-1');
INSERT INTO `student` VALUES (5, '20240102001', '$2a$10$9aIcqNKrHJbh7gEdLMESNeQ4yRrqq8ulj6.NGSQzNsIGiXyglIxke', '孙七', NULL, '男', '13912340005', 'STUDENT', 1, 'sunqi@cs.edu.cn', 'CS2024-2');
INSERT INTO `student` VALUES (6, '20240102002', '$2a$10$BWoh2FK6r4.cnNTK3QiTieJpZOWky49nuh9AbHPMSDuArNBSD0Ftu', '周八', NULL, '女', '13912340006', 'STUDENT', 1, 'zhouba@cs.edu.cn', 'CS2024-2');
INSERT INTO `student` VALUES (7, '20240102003', '$2a$10$wxPJiYiiio8lWJk/k.hN5eNXx2dGXnapDud6bEQFaE0L6AyHAP006', '吴九', NULL, '男', '13912340007', 'STUDENT', 1, 'wujiu@cs.edu.cn', 'CS2024-2');
INSERT INTO `student` VALUES (8, '20240102004', '$2a$10$gxsAid4lYgjJ1WyGYza2vOcJMI/EsneEUSQTMGXaXwUYp1LEwTeMq', '郑十', NULL, '女', '13912340008', 'STUDENT', 1, 'zhengshi@cs.edu.cn', 'CS2024-2');

-- ----------------------------
-- Table structure for student_admin_class
-- ----------------------------
DROP TABLE IF EXISTS `student_admin_class`;
CREATE TABLE `student_admin_class` (
  `id` int NOT NULL AUTO_INCREMENT,
  `student_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '学生ID',
  `admin_class_id` int NULL DEFAULT NULL COMMENT '行政班ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for student_course
-- ----------------------------
DROP TABLE IF EXISTS `student_course`;
CREATE TABLE `student_course` (
  `id` int NOT NULL AUTO_INCREMENT,
  `student_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '学生ID',
  `teaching_class_id` int NULL DEFAULT NULL COMMENT '教学班ID',
  `course_id` int NULL DEFAULT NULL COMMENT '课程ID',
  `status` int NULL DEFAULT 1 COMMENT '状态：0-已退课，1-在读',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_config
-- ----------------------------
DROP TABLE IF EXISTS `system_config`;
CREATE TABLE `system_config` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `config_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '配置键',
  `config_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '配置值',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_config_key`(`config_key` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统配置表' ROW_FORMAT = Dynamic;

INSERT INTO `system_config` VALUES (1, 'course_selection_enabled', 'true', '选课功能开关：true-开启，false-关闭');
INSERT INTO `system_config` VALUES (2, 'teacher_score_entry_enabled', 'true', '教师录入成绩开关：true-开启，false-关闭');
INSERT INTO `system_config` VALUES (3, 'makeup_exam_score_entry_enabled', 'true', '补考成绩录入开关：true-开启，false-关闭');

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `gender` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '性别',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色',
  `status` int NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `title` int NULL DEFAULT 0 COMMENT '职称：0-讲师，1-副教授，2-教授',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '教师信息' ROW_FORMAT = DYNAMIC;

INSERT INTO `teacher` VALUES (1, 'T2024001', '$2a$10$eeiPULc3FYqamJR5JuUOYecvqONoexJWHkXz/BjYF249rZJuV.qdi', '张教授', NULL, '男', '13912341001', 'TEACHER', 1, 'zhangjiaoshou@cs.edu.cn', 2);
INSERT INTO `teacher` VALUES (2, 'T2024002', '$2a$10$kpgxjUQFFOnog/E7q/xZq.E0F0UkhACpQnz2GNShQ/0QET8stCqfO', '李华', NULL, '女', '13912341002', 'TEACHER', 1, 'lihua@cs.edu.cn', 0);
INSERT INTO `teacher` VALUES (3, 'T2024003', '$2a$10$9c7DOS0BZFJHzc093XUj1eqcZkgLjurt7OkBa6ReSL84rLt6U1ygm', '王强', NULL, '男', '13912341003', 'TEACHER', 1, 'wangqiang@cs.edu.cn', 1);
INSERT INTO `teacher` VALUES (4, 'T2024004', '$2a$10$IBxpV8MZLQhkzdfadbkSWu.lwwIPZzIGpQG5C4Iom8XESAamrvUEq', '刘芳', NULL, '女', '13912341004', 'TEACHER', 1, 'liufang@cs.edu.cn', 1);
INSERT INTO `teacher` VALUES (5, 'T2024005', '$2a$10$9aIcqNKrHJbh7gEdLMESNeQ4yRrqq8ulj6.NGSQzNsIGiXyglIxke', '陈明亮', NULL, '男', '13912341005', 'TEACHER', 1, 'chenmingliang@cs.edu.cn', 0);

-- ----------------------------
-- Table structure for teaching_class
-- ----------------------------
DROP TABLE IF EXISTS `teaching_class`;
CREATE TABLE `teaching_class` (
  `id` int NOT NULL AUTO_INCREMENT,
  `class_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '教学班代码',
  `course_id` int NULL DEFAULT NULL COMMENT '课程ID',
  `academic_year_id` int NULL DEFAULT NULL COMMENT '学年ID',
  `capacity` int NULL DEFAULT NULL COMMENT '容量',
  `status` int NULL DEFAULT 1 COMMENT '状态：0-关闭，1-开放',
  `classroom` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '教室',
  `weekday` int NULL DEFAULT NULL COMMENT '上课星期',
  `start_period` int NULL DEFAULT NULL COMMENT '开始节次',
  `end_period` int NULL DEFAULT NULL COMMENT '结束节次',
  `teacher_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '教师ID',
  `teacher_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '教师姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '教学班表' ROW_FORMAT = Dynamic;

INSERT INTO `teaching_class` VALUES (1, 'MATH101-2024-1-01', 1, 1, 50, 1, 'A101', 1, 1, 2, 'T2024001', '张教授');
INSERT INTO `teaching_class` VALUES (2, 'MATH101-2024-1-02', 1, 1, 50, 1, 'A102', 3, 3, 4, 'T2024001', '张教授');
INSERT INTO `teaching_class` VALUES (3, 'CS101-2024-1-01', 3, 1, 40, 1, 'B201', 2, 1, 3, 'T2024002', '李华');
INSERT INTO `teaching_class` VALUES (4, 'CS102-2024-1-01', 4, 1, 40, 1, 'B202', 4, 1, 3, 'T2024003', '王强');
INSERT INTO `teaching_class` VALUES (5, 'CS103-2024-1-01', 5, 1, 40, 1, 'B301', 5, 1, 3, 'T2024004', '刘芳');
INSERT INTO `teaching_class` VALUES (6, 'CS104-2024-1-01', 6, 1, 40, 1, 'B302', 1, 3, 5, 'T2024005', '陈明亮');
INSERT INTO `teaching_class` VALUES (7, 'CS107-2024-1-01', 9, 1, 30, 1, 'C101', 2, 4, 6, 'T2024002', '李华');
INSERT INTO `teaching_class` VALUES (8, 'CS108-2024-1-01', 10, 1, 30, 1, 'C102', 3, 5, 7, 'T2024003', '王强');
INSERT INTO `teaching_class` VALUES (9, 'MATH102-2024-1-01', 2, 1, 50, 1, 'A103', 4, 1, 2, 'T2024001', '张教授');
INSERT INTO `teaching_class` VALUES (10, 'CS105-2024-1-01', 7, 1, 30, 1, 'C201', 5, 4, 6, 'T2024004', '刘芳');
INSERT INTO `teaching_class` VALUES (11, 'CS106-2024-1-01', 8, 1, 40, 1, 'C202', 1, 5, 7, 'T2024005', '陈明亮');
INSERT INTO `teaching_class` VALUES (12, 'CS201-2024-2-01', 11, 2, 30, 0, 'D101', 2, 1, 3, 'T2024001', '张教授');
INSERT INTO `teaching_class` VALUES (13, 'CS202-2024-2-01', 12, 2, 30, 0, 'D102', 3, 1, 3, 'T2024003', '王强');
INSERT INTO `teaching_class` VALUES (14, 'CS203-2024-2-01', 13, 2, 30, 0, 'D201', 4, 1, 3, 'T2024004', '刘芳');
INSERT INTO `teaching_class` VALUES (15, 'CS204-2024-2-01', 14, 2, 20, 0, 'D202', 5, 4, 6, 'T2024002', '李华');

SET FOREIGN_KEY_CHECKS = 1;
