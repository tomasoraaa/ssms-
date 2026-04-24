/*
 Navicat Premium Data Transfer

 Source Server         : forbishesms
 Source Server Type    : MySQL
 Source Server Version : 80042 (8.0.42)
 Source Host           : localhost:3306
 Source Schema         : sms

 Target Server Type    : MySQL
 Target Server Version : 80042 (8.0.42)
 File Encoding         : 65001

 Date: 24/04/2026 19:04:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for academic_year
-- ----------------------------
DROP TABLE IF EXISTS `academic_year`;
CREATE TABLE `academic_year`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `year` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学年，如 2024-2025',
  `semester` int NOT NULL COMMENT '学期：1-第一学期，2-第二学期',
  `start_date` date NULL DEFAULT NULL COMMENT '学期开始日期',
  `end_date` date NULL DEFAULT NULL COMMENT '学期结束日期',
  `selection_start` datetime NULL DEFAULT NULL COMMENT '选课开始时间',
  `selection_end` datetime NULL DEFAULT NULL COMMENT '选课结束时间',
  `status` int NULL DEFAULT 1 COMMENT '状态：0-未开启，1-进行中，2-已结束',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_year_semester`(`year` ASC, `semester` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '学年学期表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of academic_year
-- ----------------------------
INSERT INTO `academic_year` VALUES (1, '2024-2025', 1, '2024-09-01', '2025-01-15', '2024-08-01 00:00:00', '2024-08-31 23:59:59', 2);
INSERT INTO `academic_year` VALUES (2, '2024-2025', 2, '2025-02-20', '2025-07-15', '2025-02-01 00:00:00', '2025-02-28 23:59:59', 0);

-- ----------------------------
-- Table structure for activity_log
-- ----------------------------
DROP TABLE IF EXISTS `activity_log`;
CREATE TABLE `activity_log`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `operate_time` datetime NOT NULL,
  `operate_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `operate_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_operate_time`(`operate_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 128 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of activity_log
-- ----------------------------
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
INSERT INTO `activity_log` VALUES (15, '2026-04-19 19:39:21', '成绩', 'admin', '为学生添加课程成绩', 'ADMIN');
INSERT INTO `activity_log` VALUES (16, '2026-04-19 19:44:33', '选课', '20240101001', '学生 20240101001 申请选课', 'STUDENT');
INSERT INTO `activity_log` VALUES (17, '2026-04-19 19:44:52', '选课', 'admin', '批准选课申请', 'ADMIN');
INSERT INTO `activity_log` VALUES (18, '2026-04-19 19:45:21', '成绩', 'admin', '为学生添加课程成绩', 'ADMIN');
INSERT INTO `activity_log` VALUES (19, '2026-04-19 19:45:53', '成绩', 'admin', '为学生添加课程成绩', 'ADMIN');
INSERT INTO `activity_log` VALUES (20, '2026-04-19 19:46:06', '选课', '20240101001', '学生 20240101001 申请选课', 'STUDENT');
INSERT INTO `activity_log` VALUES (21, '2026-04-19 19:46:08', '选课', '20240101001', '学生 20240101001 申请选课', 'STUDENT');
INSERT INTO `activity_log` VALUES (22, '2026-04-19 19:46:12', '选课', 'admin', '批准选课申请', 'ADMIN');
INSERT INTO `activity_log` VALUES (23, '2026-04-19 19:46:13', '选课', 'admin', '批准选课申请', 'ADMIN');
INSERT INTO `activity_log` VALUES (24, '2026-04-19 19:50:22', '选课', '20240101001', '学生 20240101001 申请选课', 'STUDENT');
INSERT INTO `activity_log` VALUES (25, '2026-04-19 19:50:23', '选课', '20240101001', '学生 20240101001 申请选课', 'STUDENT');
INSERT INTO `activity_log` VALUES (26, '2026-04-19 19:50:32', '选课', 'admin', '批准选课申请', 'ADMIN');
INSERT INTO `activity_log` VALUES (27, '2026-04-19 19:50:33', '选课', 'admin', '批准选课申请', 'ADMIN');
INSERT INTO `activity_log` VALUES (28, '2026-04-19 20:07:57', '选课', '20240101001', '学生 20240101001 申请选课', 'STUDENT');
INSERT INTO `activity_log` VALUES (29, '2026-04-19 20:07:59', '选课', '20240101001', '学生 20240101001 申请选课', 'STUDENT');
INSERT INTO `activity_log` VALUES (30, '2026-04-19 20:08:12', '选课', 'admin', '批准选课申请', 'ADMIN');
INSERT INTO `activity_log` VALUES (31, '2026-04-19 20:08:12', '选课', 'admin', '批准选课申请', 'ADMIN');
INSERT INTO `activity_log` VALUES (32, '2026-04-19 20:08:54', '成绩', 'admin', '修改学生课程成绩', 'ADMIN');
INSERT INTO `activity_log` VALUES (33, '2026-04-19 20:10:22', '选课', '20240101001', '学生 20240101001 申请选课', 'STUDENT');
INSERT INTO `activity_log` VALUES (34, '2026-04-19 20:10:29', '选课', '20240101001', '学生 20240101001 申请选课', 'STUDENT');
INSERT INTO `activity_log` VALUES (35, '2026-04-19 20:10:38', '选课', 'admin', '拒绝选课申请', 'ADMIN');
INSERT INTO `activity_log` VALUES (36, '2026-04-19 20:10:38', '选课', 'admin', '拒绝选课申请', 'ADMIN');
INSERT INTO `activity_log` VALUES (37, '2026-04-19 21:16:41', '登录', '20240101001', '学生 20240101001 登录系统', 'STUDENT');
INSERT INTO `activity_log` VALUES (38, '2026-04-19 21:20:33', '登录', '20240101002', '学生 20240101002 登录系统', 'STUDENT');
INSERT INTO `activity_log` VALUES (39, '2026-04-19 21:24:46', '登录', 'T2024002', '教师 T2024002 登录系统', 'TEACHER');
INSERT INTO `activity_log` VALUES (40, '2026-04-19 21:37:29', '成绩', 'admin', '修改学生课程成绩', 'ADMIN');
INSERT INTO `activity_log` VALUES (41, '2026-04-19 21:38:08', '选课', '20240101001', '学生 20240101001 申请选课', 'STUDENT');
INSERT INTO `activity_log` VALUES (42, '2026-04-19 21:38:14', '选课', 'admin', '批准选课申请', 'ADMIN');
INSERT INTO `activity_log` VALUES (43, '2026-04-19 21:38:29', '退课', '20240101001', '学生 20240101001 提交退课申请', 'STUDENT');
INSERT INTO `activity_log` VALUES (44, '2026-04-19 21:44:59', '退课', 'admin', '批准退课申请', 'ADMIN');
INSERT INTO `activity_log` VALUES (45, '2026-04-19 22:03:07', '用户', 'admin', '批量导入学生', 'ADMIN');
INSERT INTO `activity_log` VALUES (46, '2026-04-21 12:38:40', '登录', 'admin', '管理员 admin 登录系统', 'ADMIN');
INSERT INTO `activity_log` VALUES (47, '2026-04-21 12:41:03', '系统', 'admin', '修改学年学期 2024-2025 第1学期', 'ADMIN');
INSERT INTO `activity_log` VALUES (48, '2026-04-21 15:29:47', '登录', '20240101001', '学生 20240101001 登录系统', 'STUDENT');
INSERT INTO `activity_log` VALUES (49, '2026-04-21 18:38:16', '登录', '20240101001', '学生 20240101001 登录系统', 'STUDENT');
INSERT INTO `activity_log` VALUES (50, '2026-04-21 18:59:57', '系统', 'admin', '新增行政班 2025级计算机科学与技术1班', 'ADMIN');
INSERT INTO `activity_log` VALUES (51, '2026-04-21 19:17:59', '系统', 'admin', '删除教学班 null', 'ADMIN');
INSERT INTO `activity_log` VALUES (52, '2026-04-21 19:18:07', '系统', 'admin', '删除教学班 null', 'ADMIN');
INSERT INTO `activity_log` VALUES (53, '2026-04-21 19:18:11', '系统', 'admin', '删除教学班 null', 'ADMIN');
INSERT INTO `activity_log` VALUES (54, '2026-04-21 19:18:13', '系统', 'admin', '删除教学班 null', 'ADMIN');
INSERT INTO `activity_log` VALUES (55, '2026-04-21 19:32:58', '系统', 'admin', '修改教学班 CS104-2024-1-01', 'ADMIN');
INSERT INTO `activity_log` VALUES (56, '2026-04-21 21:54:53', '系统', 'admin', '新增教学班 CS40.-2024-2-01', 'ADMIN');
INSERT INTO `activity_log` VALUES (57, '2026-04-21 21:54:59', '系统', 'admin', '修改教学班 CS40.-2024-2-01', 'ADMIN');
INSERT INTO `activity_log` VALUES (58, '2026-04-21 22:00:24', '系统', 'admin', '修改教学班 CS40.-2024-2-01', 'ADMIN');
INSERT INTO `activity_log` VALUES (59, '2026-04-21 22:00:30', '系统', 'admin', '修改教学班 CS40.-2024-2-01', 'ADMIN');
INSERT INTO `activity_log` VALUES (60, '2026-04-21 22:00:56', '系统', 'admin', '修改教学班 CS40.-2024-2-01', 'ADMIN');
INSERT INTO `activity_log` VALUES (61, '2026-04-21 22:01:27', '系统', 'admin', '修改教学班 CS40.-2024-2-01', 'ADMIN');
INSERT INTO `activity_log` VALUES (62, '2026-04-21 22:18:28', '系统', 'admin', '新增教学班 CS403-2024-2-02', 'ADMIN');
INSERT INTO `activity_log` VALUES (63, '2026-04-21 22:18:33', '系统', 'admin', '修改教学班 CS403-2024-2-02', 'ADMIN');
INSERT INTO `activity_log` VALUES (64, '2026-04-21 22:18:53', '系统', 'admin', '修改教学班 CS403-2024-2-02', 'ADMIN');
INSERT INTO `activity_log` VALUES (65, '2026-04-21 22:19:01', '系统', 'admin', '修改教学班 CS403-2024-2-02', 'ADMIN');
INSERT INTO `activity_log` VALUES (66, '2026-04-21 22:21:03', '系统', 'admin', '修改教学班 CS403-2024-2-02', 'ADMIN');
INSERT INTO `activity_log` VALUES (67, '2026-04-21 22:21:06', '系统', 'admin', '修改教学班 CS403-2024-2-02', 'ADMIN');
INSERT INTO `activity_log` VALUES (68, '2026-04-21 22:21:10', '系统', 'admin', '修改教学班 CS403-2024-2-02', 'ADMIN');
INSERT INTO `activity_log` VALUES (69, '2026-04-21 22:37:12', '选课', '20240101001', '学生 20240101001 申请选课', 'STUDENT');
INSERT INTO `activity_log` VALUES (70, '2026-04-21 22:37:25', '选课', 'admin', '批准选课申请', 'ADMIN');
INSERT INTO `activity_log` VALUES (71, '2026-04-21 22:42:22', '登录', 'T2024020', '教师 T2024020 登录系统', 'TEACHER');
INSERT INTO `activity_log` VALUES (72, '2026-04-21 23:22:03', '系统', 'admin', '修改教学班 CS403-2024-2-01', 'ADMIN');
INSERT INTO `activity_log` VALUES (73, '2026-04-22 13:36:51', '登录', 'admin', '管理员 admin 登录系统', 'ADMIN');
INSERT INTO `activity_log` VALUES (74, '2026-04-22 13:39:05', '登录', 'admin', '管理员 admin 登录系统', 'ADMIN');
INSERT INTO `activity_log` VALUES (75, '2026-04-22 13:39:34', '登录', '20240101001', '学生 20240101001 登录系统', 'STUDENT');
INSERT INTO `activity_log` VALUES (76, '2026-04-22 13:42:30', '登录', 'T2024020', '教师 T2024020 登录系统', 'TEACHER');
INSERT INTO `activity_log` VALUES (77, '2026-04-22 13:43:41', '系统', 'admin', '修改教学班 CS104-2024-1-01', 'ADMIN');
INSERT INTO `activity_log` VALUES (78, '2026-04-22 13:46:25', '系统', 'admin', '修改教学班 CS403-2024-2-01', 'ADMIN');
INSERT INTO `activity_log` VALUES (79, '2026-04-22 13:46:56', '系统', 'admin', '修改教学班 CS403-2024-2-01', 'ADMIN');
INSERT INTO `activity_log` VALUES (80, '2026-04-22 13:47:07', '系统', 'admin', '修改教学班 CS403-2024-2-02', 'ADMIN');
INSERT INTO `activity_log` VALUES (81, '2026-04-22 13:55:43', '系统', 'admin', '修改教学班 CS403-2024-2-02', 'ADMIN');
INSERT INTO `activity_log` VALUES (82, '2026-04-22 13:56:10', '系统', 'admin', '修改教学班 CS104-2024-1-01', 'ADMIN');
INSERT INTO `activity_log` VALUES (83, '2026-04-22 13:57:28', '系统', 'admin', '新增教学班 CS402-2024-2-01', 'ADMIN');
INSERT INTO `activity_log` VALUES (84, '2026-04-22 13:57:34', '系统', 'admin', '修改教学班 CS402-2024-2-01', 'ADMIN');
INSERT INTO `activity_log` VALUES (85, '2026-04-22 14:11:57', '系统', 'admin', '新增教学班 CS304-2024-2-1', 'ADMIN');
INSERT INTO `activity_log` VALUES (86, '2026-04-22 14:20:07', '系统', 'admin', '删除教学班 CS304-2024-2-1', 'ADMIN');
INSERT INTO `activity_log` VALUES (87, '2026-04-22 14:21:14', '系统', 'admin', '新增教学班 CS304-2024-2-01', 'ADMIN');
INSERT INTO `activity_log` VALUES (88, '2026-04-22 14:48:39', '系统', 'admin', '修改教学班 CS304-2024-2-01', 'ADMIN');
INSERT INTO `activity_log` VALUES (89, '2026-04-22 14:49:14', '系统', 'admin', '修改教学班 CS402-2024-2-01', 'ADMIN');
INSERT INTO `activity_log` VALUES (90, '2026-04-22 14:49:40', '系统', 'admin', '修改教学班 CS403-2024-2-02', 'ADMIN');
INSERT INTO `activity_log` VALUES (91, '2026-04-22 14:49:43', '系统', 'admin', '修改教学班 CS403-2024-2-01', 'ADMIN');
INSERT INTO `activity_log` VALUES (92, '2026-04-22 14:50:38', '系统', 'admin', '修改教学班 CS104-2024-1-01', 'ADMIN');
INSERT INTO `activity_log` VALUES (93, '2026-04-22 15:06:47', '退课', '20240101001', '学生 20240101001 提交退课申请', 'STUDENT');
INSERT INTO `activity_log` VALUES (94, '2026-04-22 15:06:52', '退课', 'admin', '批准退课申请', 'ADMIN');
INSERT INTO `activity_log` VALUES (95, '2026-04-22 15:28:04', '登录', '20240101001', '学生 20240101001 登录系统', 'STUDENT');
INSERT INTO `activity_log` VALUES (96, '2026-04-22 15:28:20', '登录', '20240101002', '学生 20240101002 登录系统', 'STUDENT');
INSERT INTO `activity_log` VALUES (97, '2026-04-22 15:31:20', '选课', '20240101002', '学生 20240101002 申请选课', 'STUDENT');
INSERT INTO `activity_log` VALUES (98, '2026-04-22 15:31:30', '选课', '20240101001', '学生 20240101001 申请选课', 'STUDENT');
INSERT INTO `activity_log` VALUES (99, '2026-04-22 16:07:21', '系统', 'admin', '修改教学班 CS104-2024-1-01', 'ADMIN');
INSERT INTO `activity_log` VALUES (100, '2026-04-22 17:55:11', '登录', 'T2024004', '教师 T2024004 登录系统', 'TEACHER');
INSERT INTO `activity_log` VALUES (101, '2026-04-22 17:55:43', '登录', '20240101001', '学生 20240101001 登录系统', 'STUDENT');
INSERT INTO `activity_log` VALUES (102, '2026-04-22 18:09:19', '系统', 'admin', '新增教学班 CS403-2024-2-03', 'ADMIN');
INSERT INTO `activity_log` VALUES (103, '2026-04-22 18:30:37', '用户', 'admin', '新增学生 马嘉祺', 'ADMIN');
INSERT INTO `activity_log` VALUES (104, '2026-04-22 19:12:24', '用户', 'admin', '新增教师 张明', 'ADMIN');
INSERT INTO `activity_log` VALUES (105, '2026-04-22 19:12:59', '用户', 'admin', '删除教师 张明', 'ADMIN');
INSERT INTO `activity_log` VALUES (106, '2026-04-22 19:16:12', '成绩', 'admin', '修改学生课程成绩', 'ADMIN');
INSERT INTO `activity_log` VALUES (107, '2026-04-22 19:16:12', '成绩', 'admin', '修改学生课程成绩', 'ADMIN');
INSERT INTO `activity_log` VALUES (108, '2026-04-22 19:21:14', '登录', '20240101003', '学生 20240101003 登录系统', 'STUDENT');
INSERT INTO `activity_log` VALUES (109, '2026-04-22 19:21:27', '选课', '20240101003', '学生 20240101003 申请选课', 'STUDENT');
INSERT INTO `activity_log` VALUES (110, '2026-04-22 19:22:01', '成绩', 'admin', '修改学生课程成绩', 'ADMIN');
INSERT INTO `activity_log` VALUES (111, '2026-04-22 19:22:01', '成绩', 'admin', '修改学生课程成绩', 'ADMIN');
INSERT INTO `activity_log` VALUES (112, '2026-04-22 19:32:51', '成绩', 'admin', '修改学生课程成绩', 'ADMIN');
INSERT INTO `activity_log` VALUES (113, '2026-04-22 20:33:23', '成绩', 'admin', '为学生添加课程成绩', 'ADMIN');
INSERT INTO `activity_log` VALUES (114, '2026-04-22 20:53:46', '选课', '20240101001', '学生 20240101001 申请选课', 'STUDENT');
INSERT INTO `activity_log` VALUES (115, '2026-04-22 21:45:53', '成绩', 'admin', '修改学生课程成绩', 'ADMIN');
INSERT INTO `activity_log` VALUES (116, '2026-04-22 21:46:24', '成绩', 'admin', '修改学生课程成绩', 'ADMIN');
INSERT INTO `activity_log` VALUES (117, '2026-04-22 22:19:53', '成绩', 'admin', '修改学生课程成绩', 'ADMIN');
INSERT INTO `activity_log` VALUES (118, '2026-04-22 22:46:17', '登录', '20240101003', '学生 20240101003 登录系统', 'STUDENT');
INSERT INTO `activity_log` VALUES (119, '2026-04-22 23:07:24', '登录', 'admin', '管理员 admin 登录系统', 'ADMIN');
INSERT INTO `activity_log` VALUES (120, '2026-04-22 23:07:42', '登录', '20240101003', '学生 20240101003 登录系统', 'STUDENT');
INSERT INTO `activity_log` VALUES (121, '2026-04-24 14:32:28', '登录', 'admin', '管理员 admin 登录系统', 'ADMIN');
INSERT INTO `activity_log` VALUES (122, '2026-04-24 14:39:32', '登录', '20240101003', '学生 20240101003 登录系统', 'STUDENT');
INSERT INTO `activity_log` VALUES (123, '2026-04-24 14:40:22', '登录', 'T2024004', '教师 T2024004 登录系统', 'TEACHER');
INSERT INTO `activity_log` VALUES (124, '2026-04-24 14:40:25', '登录', 'T2024004', '教师 T2024004 登录系统', 'TEACHER');
INSERT INTO `activity_log` VALUES (125, '2026-04-24 14:40:39', '登录', 'T2024004', '教师 T2024004 登录系统', 'TEACHER');
INSERT INTO `activity_log` VALUES (126, '2026-04-24 17:59:29', '用户', 'admin', '修改教师 null 的信息', 'ADMIN');
INSERT INTO `activity_log` VALUES (127, '2026-04-24 18:00:09', '登录', 'T2024004', '教师 T2024004 登录系统', 'TEACHER');

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'admin', '123456', '管理员', 'http://localhost:9090/files/download/1776597372987-01c539bbcd0e069fb6775758d32e7f.jpg', 'ADMIN');

-- ----------------------------
-- Table structure for admin_class
-- ----------------------------
DROP TABLE IF EXISTS `admin_class`;
CREATE TABLE `admin_class`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `grade` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '年级，如 2024',
  `major` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '专业',
  `class_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '班级名称，如 计算机1班',
  `class_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '班级代码，如 CS2024-1',
  `counselor_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '辅导员/班主任工号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_class_code`(`class_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '行政班表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin_class
-- ----------------------------
INSERT INTO `admin_class` VALUES (1, '2024', '计算机科学与技术', '2024级计算机1班', 'CS2024-1', 'T2024001');
INSERT INTO `admin_class` VALUES (2, '2024', '计算机科学与技术', '2024级计算机2班', 'CS2024-2', 'T2024002');
INSERT INTO `admin_class` VALUES (3, '2024', '软件工程', '2024级软件工程1班', 'SE2024-1', 'T2024003');
INSERT INTO `admin_class` VALUES (4, '2023', '计算机科学与技术', '2023级计算机1班', 'CS2023-1', 'T2024004');
INSERT INTO `admin_class` VALUES (5, '2023', '软件工程', '2023级软件工程1班', 'SE2023-1', 'T2024005');
INSERT INTO `admin_class` VALUES (9, '2025', '计算机科学与技术', '2025级计算机科学与技术1班', 'CS2025-1', 'T2024006');

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `course_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `course_code` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `credit` int NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES (1, 'C语言程序设计', 'CS101', 4, '本课程主要讲解C语言的基本语法、程序结构和编程方法，培养学生的编程思维和动手能力。');
INSERT INTO `course` VALUES (2, '数据结构', 'CS102', 4, '本课程教授线性表、栈、队列、树、图等基本数据结构的原理和实现方法，以及常用排序和查找算法。');
INSERT INTO `course` VALUES (3, 'Java程序设计', 'CS201', 4, '本课程介绍Java语言的核心概念、面向对象编程思想和常用类库，培养学生开发桌面应用和Web应用的能力。');
INSERT INTO `course` VALUES (4, '算法设计与分析', 'CS202', 3, '本课程讲解常用算法的设计策略、复杂度分析方法，以及动态规划、贪心算法、分治法等核心算法思想。');
INSERT INTO `course` VALUES (5, '离散数学', 'CS103', 3, '本课程涵盖集合论、图论、数理逻辑、布尔代数等离散数学基础知识，为计算机专业学习奠定理论基础。');
INSERT INTO `course` VALUES (6, '数据库原理与应用', 'CS203', 4, '本课程介绍关系型数据库的基本理论、SQL语言、数据库设计方法，以及MySQL等主流数据库的实际应用。');
INSERT INTO `course` VALUES (7, '计算机网络', 'CS204', 3, '本课程讲解OSI参考模型、TCP/IP协议栈、网络编程基础知识，以及局域网和互联网技术的原理与应用。');
INSERT INTO `course` VALUES (8, '操作系统', 'CS205', 4, '本课程介绍操作系统的基本概念、进程管理、内存管理、文件系统、设备管理等核心模块的原理与实现。');
INSERT INTO `course` VALUES (9, '软件工程', 'CS301', 3, '本课程涵盖软件工程的生命周期模型、需求分析、设计模式、软件测试、项目管理等理论知识和实践方法。');
INSERT INTO `course` VALUES (10, '编译原理', 'CS302', 3, '本课程讲解编译器的结构、各阶段工作原理，包括词法分析、语法分析、语义分析、代码生成与优化等。');
INSERT INTO `course` VALUES (11, '计算机组成原理', 'CS104', 4, '本课程介绍计算机硬件系统的组成结构、工作原理，包括数据的表示、运算器、存储器、指令系统等。');
INSERT INTO `course` VALUES (12, 'Python程序设计', 'CS105', 3, '本课程教授Python语言的基础语法、数据类型、控制结构，以及NumPy、Pandas等数据科学库的实战应用。');
INSERT INTO `course` VALUES (13, 'Web应用开发', 'CS303', 3, '本课程讲解HTML、CSS、JavaScript前端技术，以及Servlet、JSP、Ajax等Java Web开发的核心技术。');
INSERT INTO `course` VALUES (14, '人工智能导论', 'CS401', 3, '本课程介绍人工智能的基本概念、机器学习基础、神经网络原理，以及深度学习在图像和语音领域的应用。');
INSERT INTO `course` VALUES (15, '数据挖掘与分析', 'CS402', 3, '本课程涵盖数据预处理、分类算法、聚类分析、关联规则挖掘等数据科学方法，以及大数据处理技术。');
INSERT INTO `course` VALUES (16, '信息安全基础', 'CS303', 3, '本课程介绍网络安全、密码学基础、入侵检测、防火墙技术等信息安全领域的核心知识和防护策略。');
INSERT INTO `course` VALUES (17, '云计算与大数据', 'CS403', 3, '本课程讲解云计算的架构与原理、Hadoop和Spark等大数据处理框架，以及分布式存储和计算技术。');
INSERT INTO `course` VALUES (18, '计算机图形学', 'CS304', 3, '本课程涵盖图形学的基本算法、图形变换、渲染技术，以及OpenGL编程和三维图形的生成方法。');
INSERT INTO `course` VALUES (19, '软件测试与质量保障', 'CS305', 2, '本课程介绍软件测试的基本方法、测试用例设计、自动化测试框架，以及软件质量评估体系。');
INSERT INTO `course` VALUES (20, '课程设计', 'CS401', 4, '本课程是一门综合性实践课程，要求学生完成一个完整软件项目的需求分析、设计、编码和测试工作。');

-- ----------------------------
-- Table structure for course_evaluation
-- ----------------------------
DROP TABLE IF EXISTS `course_evaluation`;
CREATE TABLE `course_evaluation`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `student_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `student_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `course_id` int NOT NULL,
  `course_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `teacher_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `rating` int NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `teacher_evaluation` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `evaluation_time` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_student_course`(`student_id` ASC, `course_id` ASC) USING BTREE,
  INDEX `idx_course_id`(`course_id` ASC) USING BTREE,
  INDEX `idx_teacher_id`(`teacher_id` ASC) USING BTREE,
  INDEX `idx_course_teacher`(`course_id` ASC, `teacher_id` ASC) USING BTREE,
  CONSTRAINT `fk_course_evaluation_course` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_course_evaluation_student` FOREIGN KEY (`student_id`) REFERENCES `student` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_course_evaluation_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_evaluation
-- ----------------------------
INSERT INTO `course_evaluation` VALUES (2, '20240101001', '张梓萱', 17, '云计算与大数据', 'T2024004', 5, '很好', '很认真', '2026-04-22 18:12:04');

-- ----------------------------
-- Table structure for course_selection
-- ----------------------------
DROP TABLE IF EXISTS `course_selection`;
CREATE TABLE `course_selection`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `user_type` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `course_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `course_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `status` int NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `teacher_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `teacher_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `teaching_class_code` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `teaching_class_id` int NULL DEFAULT NULL,
  `academic_year_id` int NULL DEFAULT NULL,
  `admin_class_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_course`(`user_id` ASC, `course_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_selection
-- ----------------------------
INSERT INTO `course_selection` VALUES (16, '20240101002', '李浩然', 'STUDENT', '17', '云计算与大数据', 1, '2026-04-22 15:31:19', 'T2024004', '刘芳', 'CS403-2024-2-02', 7, NULL, NULL);
INSERT INTO `course_selection` VALUES (18, '20240101003', '王思琪', 'STUDENT', '17', '云计算与大数据', 1, '2026-04-22 19:21:26', 'T2024004', '刘芳', 'CS403-2024-2-03', 12, NULL, NULL);
INSERT INTO `course_selection` VALUES (19, '20240101001', '张梓萱', 'STUDENT', '17', '云计算与大数据', 1, '2026-04-22 20:53:45', 'T2024004', '刘芳', 'CS403-2024-2-02', 7, NULL, NULL);

-- ----------------------------
-- Table structure for course_teacher
-- ----------------------------
DROP TABLE IF EXISTS `course_teacher`;
CREATE TABLE `course_teacher`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `course_id` int NOT NULL COMMENT '课程ID',
  `teacher_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '教师工号',
  `teacher_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '教师姓名',
  `is_main_teacher` int NULL DEFAULT 0 COMMENT '是否主讲教师：0-普通教师，1-主讲教师',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_course_id`(`course_id` ASC) USING BTREE,
  INDEX `idx_teacher_id`(`teacher_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 57 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程教师关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_teacher
-- ----------------------------
INSERT INTO `course_teacher` VALUES (1, 1, 'T2024001', '张明', 1);
INSERT INTO `course_teacher` VALUES (2, 1, 'T2024004', '刘芳', 1);
INSERT INTO `course_teacher` VALUES (3, 2, 'T2024002', '李华', 1);
INSERT INTO `course_teacher` VALUES (4, 2, 'T2024005', '陈明亮', 1);
INSERT INTO `course_teacher` VALUES (5, 3, 'T2024003', '王强', 1);
INSERT INTO `course_teacher` VALUES (6, 3, 'T2024006', '赵小红', 1);
INSERT INTO `course_teacher` VALUES (7, 4, 'T2024007', '钱建国', 1);
INSERT INTO `course_teacher` VALUES (8, 5, 'T2024008', '孙丽', 1);
INSERT INTO `course_teacher` VALUES (9, 6, 'T2024009', '周建国', 1);
INSERT INTO `course_teacher` VALUES (10, 6, 'T2024010', '吴敏', 1);
INSERT INTO `course_teacher` VALUES (11, 7, 'T2024011', '郑华', 1);
INSERT INTO `course_teacher` VALUES (12, 8, 'T2024012', '黄丽', 1);
INSERT INTO `course_teacher` VALUES (13, 8, 'T2024013', '马建国', 1);
INSERT INTO `course_teacher` VALUES (14, 9, 'T2024014', '朱敏', 1);
INSERT INTO `course_teacher` VALUES (15, 10, 'T2024015', '胡华', 1);
INSERT INTO `course_teacher` VALUES (16, 11, 'T2024016', '林丽', 1);
INSERT INTO `course_teacher` VALUES (17, 11, 'T2024017', '郭建国', 1);
INSERT INTO `course_teacher` VALUES (18, 12, 'T2024018', '梁敏', 1);
INSERT INTO `course_teacher` VALUES (19, 13, 'T2024019', '谢华', 1);
INSERT INTO `course_teacher` VALUES (20, 14, 'T2024020', '宋丽', 1);
INSERT INTO `course_teacher` VALUES (21, 14, 'T2024001', '张明', 1);
INSERT INTO `course_teacher` VALUES (22, 15, 'T2024002', '李华', 1);
INSERT INTO `course_teacher` VALUES (23, 16, 'T2024003', '王强', 1);
INSERT INTO `course_teacher` VALUES (24, 17, 'T2024004', '刘芳', 1);
INSERT INTO `course_teacher` VALUES (25, 18, 'T2024005', '陈明亮', 1);
INSERT INTO `course_teacher` VALUES (26, 19, 'T2024006', '赵小红', 1);
INSERT INTO `course_teacher` VALUES (27, 20, 'T2024007', '钱建国', 1);
INSERT INTO `course_teacher` VALUES (28, 20, 'T2024008', '孙丽', 1);
INSERT INTO `course_teacher` VALUES (49, 17, 'T2024004', '刘芳', 1);
INSERT INTO `course_teacher` VALUES (51, 17, 'T2024020', '宋丽', 1);
INSERT INTO `course_teacher` VALUES (52, 11, 'T2024016', '林丽', 1);
INSERT INTO `course_teacher` VALUES (53, 15, 'T2024002', '李华', 1);
INSERT INTO `course_teacher` VALUES (54, 15, 'T2024002', '李华', 1);
INSERT INTO `course_teacher` VALUES (55, 18, 'T2024005', '陈明亮', 1);
INSERT INTO `course_teacher` VALUES (56, 18, 'T2024005', '陈明亮', 1);

-- ----------------------------
-- Table structure for course_withdrawal
-- ----------------------------
DROP TABLE IF EXISTS `course_withdrawal`;
CREATE TABLE `course_withdrawal`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `student_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `course_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `teacher_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `teacher_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `teaching_class_id` int NULL DEFAULT NULL COMMENT '教学班ID',
  `withdrawal_time` datetime NULL DEFAULT NULL,
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `status` int NULL DEFAULT 0,
  `reason_type` int NULL DEFAULT NULL COMMENT '退课原因类型：1-自愿退课，2-不及格退课，3-其他',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_withdrawal
-- ----------------------------

-- ----------------------------
-- Table structure for makeup_exam
-- ----------------------------
DROP TABLE IF EXISTS `makeup_exam`;
CREATE TABLE `makeup_exam`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `student_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `course_id` int NULL DEFAULT NULL,
  `teaching_class_id` int NULL DEFAULT NULL,
  `exam_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `makeup_score` decimal(5, 2) NULL DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '待审批',
  `reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `student_id`(`student_id` ASC) USING BTREE,
  INDEX `course_id`(`course_id` ASC) USING BTREE,
  INDEX `teaching_class_id`(`teaching_class_id` ASC) USING BTREE,
  CONSTRAINT `makeup_exam_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`username`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `makeup_exam_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `makeup_exam_ibfk_3` FOREIGN KEY (`teaching_class_id`) REFERENCES `teaching_class` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of makeup_exam
-- ----------------------------
INSERT INTO `makeup_exam` VALUES (1, '20240101003', 17, 12, '补考', 60.00, '已通过', '挂科了，按照规定需要申请补考', '2026-04-22 22:47:15', '2026-04-24 18:02:27');

-- ----------------------------
-- Table structure for modify_request
-- ----------------------------
DROP TABLE IF EXISTS `modify_request`;
CREATE TABLE `modify_request`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `user_type` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `field_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `old_value` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `new_value` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `status` int NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of modify_request
-- ----------------------------
INSERT INTO `modify_request` VALUES (1, '20240101001', '张梓萱', 'STUDENT', 'avatar', '', 'http://localhost:9090/files/download/1776589631128-f9db561dbcc6f77c0e70a1208921b5.jpg', 1, '2026-04-19 17:07:12');
INSERT INTO `modify_request` VALUES (2, 'T2024001', '张明', 'TEACHER', 'avatar', '', 'http://localhost:9090/files/download/1776596101549-0d19b52d7e475569095d0400858eec.jpg', 1, '2026-04-19 18:55:02');
INSERT INTO `modify_request` VALUES (6, 'T2024020', '宋丽', 'TEACHER', 'avatar', '', 'http://localhost:9090/files/download/1776844315693-0ff7e2e97b44d74ef25052bd7294fb.jpg', 1, '2026-04-22 15:51:56');
INSERT INTO `modify_request` VALUES (7, '20240101002', '李浩然', 'STUDENT', 'avatar', '', 'http://localhost:9090/files/download/1776844339119-1c62775b7a6fdc7e6ef35af2aac3bf.jpg', 1, '2026-04-22 15:52:19');

-- ----------------------------
-- Table structure for score_detail
-- ----------------------------
DROP TABLE IF EXISTS `score_detail`;
CREATE TABLE `score_detail`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `student_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `course_id` int NULL DEFAULT NULL,
  `teaching_class_id` int NULL DEFAULT NULL,
  `usual_score` decimal(5, 2) NULL DEFAULT NULL,
  `midterm_score` decimal(5, 2) NULL DEFAULT NULL,
  `final_score` decimal(5, 2) NULL DEFAULT NULL,
  `total_score` decimal(5, 2) NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `student_id`(`student_id` ASC) USING BTREE,
  INDEX `course_id`(`course_id` ASC) USING BTREE,
  INDEX `teaching_class_id`(`teaching_class_id` ASC) USING BTREE,
  CONSTRAINT `score_detail_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`username`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `score_detail_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `score_detail_ibfk_3` FOREIGN KEY (`teaching_class_id`) REFERENCES `teaching_class` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of score_detail
-- ----------------------------
INSERT INTO `score_detail` VALUES (1, '20240101001', 17, 7, 80.00, 90.00, 85.00, 84.50, '2026-04-22 21:45:52', '2026-04-22 21:45:52');
INSERT INTO `score_detail` VALUES (2, '20240101002', 17, 7, 90.00, 85.00, 90.00, 89.00, '2026-04-22 21:46:24', '2026-04-22 21:46:24');
INSERT INTO `score_detail` VALUES (3, '20240101003', 17, 12, 80.00, 60.00, 30.00, 49.00, '2026-04-22 22:19:53', '2026-04-22 22:19:53');

-- ----------------------------
-- Table structure for score_rule
-- ----------------------------
DROP TABLE IF EXISTS `score_rule`;
CREATE TABLE `score_rule`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `course_id` int NULL DEFAULT NULL,
  `usual_weight` int NULL DEFAULT 30,
  `midterm_weight` int NULL DEFAULT 20,
  `final_weight` int NULL DEFAULT 50,
  `warning_threshold` int NULL DEFAULT 60,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_course`(`course_id` ASC) USING BTREE,
  CONSTRAINT `score_rule_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of score_rule
-- ----------------------------
INSERT INTO `score_rule` VALUES (1, 17, 20, 30, 50, 60, '2026-04-22 22:03:09', '2026-04-22 22:15:21');
INSERT INTO `score_rule` VALUES (2, 15, 20, 30, 50, 60, '2026-04-22 22:18:56', '2026-04-22 22:18:56');

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `age` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '年龄',
  `gender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '性别',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `profession` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '专业',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色',
  `status` int NULL DEFAULT 0 COMMENT '审核状态：0-待审核，1-已通过，2-已拒绝',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '学生信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES (1, '20240101001', '123456', '张梓萱', 'http://localhost:9090/files/download/1776589631128-f9db561dbcc6f77c0e70a1208921b5.jpg', '19', '女', '13812340001', '计算机科学与技术', 'STUDENT', 1, 'zhangzx@sms.edu.cn');
INSERT INTO `student` VALUES (2, '20240101002', '123456', '李浩然', 'http://localhost:9090/files/download/1776844339119-1c62775b7a6fdc7e6ef35af2aac3bf.jpg', '19', '男', '13812340002', '计算机科学与技术', 'STUDENT', 1, 'lihr@sms.edu.cn');
INSERT INTO `student` VALUES (3, '20240101003', '123456', '王思琪', NULL, '19', '女', '13812340003', '计算机科学与技术', 'STUDENT', 1, 'wangsq@sms.edu.cn');
INSERT INTO `student` VALUES (4, '20240102001', '123456', '刘子墨', NULL, '19', '男', '13812340004', '软件工程', 'STUDENT', 1, 'liuzm@sms.edu.cn');
INSERT INTO `student` VALUES (5, '20240102002', '123456', '陈雨萱', NULL, '19', '女', '13812340005', '软件工程', 'STUDENT', 1, 'chenyx@sms.edu.cn');
INSERT INTO `student` VALUES (6, '20240201001', '123456', '赵晨曦', NULL, '19', '男', '13812340006', '网络工程', 'STUDENT', 1, 'zhaocx@sms.edu.cn');
INSERT INTO `student` VALUES (7, '20240201002', '123456', '周雅婷', NULL, '19', '女', '13812340007', '网络工程', 'STUDENT', 1, 'zhouyt@sms.edu.cn');
INSERT INTO `student` VALUES (8, '20240301001', '123456', '吴俊杰', NULL, '19', '男', '13812340008', '信息安全', 'STUDENT', 1, 'wujj@sms.edu.cn');
INSERT INTO `student` VALUES (9, '20240301002', '123456', '郑诗涵', NULL, '19', '女', '13812340009', '信息安全', 'STUDENT', 1, 'zhengsh@sms.edu.cn');
INSERT INTO `student` VALUES (10, '20240401001', '123456', '孙浩然', NULL, '19', '男', '13812340010', '数据科学', 'STUDENT', 1, 'sunhr@sms.edu.cn');
INSERT INTO `student` VALUES (11, '20240401002', '123456', '黄思远', NULL, '19', '男', '13812340011', '数据科学', 'STUDENT', 1, 'huangsy@sms.edu.cn');
INSERT INTO `student` VALUES (12, '20240402001', '123456', '林可欣', NULL, '19', '女', '13812340012', '人工智能', 'STUDENT', 1, 'linkx@sms.edu.cn');
INSERT INTO `student` VALUES (13, '20240402002', '123456', '徐子轩', NULL, '19', '男', '13812340013', '人工智能', 'STUDENT', 1, 'xuzx@sms.edu.cn');
INSERT INTO `student` VALUES (14, '20230101001', '123456', '马欣怡', NULL, '21', '女', '13812340014', '计算机科学与技术', 'STUDENT', 1, 'maxy@sms.edu.cn');
INSERT INTO `student` VALUES (15, '20230101002', '123456', '朱俊豪', NULL, '21', '男', '13812340015', '计算机科学与技术', 'STUDENT', 1, 'zhujh@sms.edu.cn');
INSERT INTO `student` VALUES (16, '20230102001', '123456', '胡夏沫', NULL, '21', '女', '13812340016', '软件工程', 'STUDENT', 1, 'huxm@sms.edu.cn');
INSERT INTO `student` VALUES (17, '20230201001', '123456', '郭宇轩', NULL, '21', '男', '13812340017', '网络工程', 'STUDENT', 1, 'guoyx@sms.edu.cn');
INSERT INTO `student` VALUES (18, '20230201002', '123456', '梁雅静', NULL, '21', '女', '13812340018', '网络工程', 'STUDENT', 1, 'liangyj@sms.edu.cn');
INSERT INTO `student` VALUES (19, '20230301001', '123456', '叶志豪', NULL, '21', '男', '13812340019', '信息安全', 'STUDENT', 1, 'yezh@sms.edu.cn');
INSERT INTO `student` VALUES (20, '20230301002', '123456', '冯雪琪', NULL, '21', '女', '13812340020', '信息安全', 'STUDENT', 1, 'fengxq@sms.edu.cn');
INSERT INTO `student` VALUES (21, '20250101001', '123456', '李梦琪', NULL, '18', '女', '138123400021', '计算机科学与技术', 'STUDENT', 1, NULL);
INSERT INTO `student` VALUES (22, '20250101002', '123456', '王浩然', NULL, '18', '男', '13812340022', '计算机科学与技术', 'STUDENT', 1, NULL);

-- ----------------------------
-- Table structure for student_admin_class
-- ----------------------------
DROP TABLE IF EXISTS `student_admin_class`;
CREATE TABLE `student_admin_class`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学号',
  `admin_class_id` int NOT NULL COMMENT '行政班ID',
  `enroll_year` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '入学年份',
  `status` int NULL DEFAULT 1 COMMENT '状态：1-在读，2-休学，3-退学，4-毕业',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_student_class`(`student_id` ASC, `admin_class_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '学生行政班关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student_admin_class
-- ----------------------------
INSERT INTO `student_admin_class` VALUES (1, '20240101001', 1, '2024', 1);
INSERT INTO `student_admin_class` VALUES (2, '20240101002', 1, '2024', 1);
INSERT INTO `student_admin_class` VALUES (3, '20240101003', 1, '2024', 1);
INSERT INTO `student_admin_class` VALUES (4, '20240102001', 2, '2024', 1);
INSERT INTO `student_admin_class` VALUES (5, '20240102002', 2, '2024', 1);
INSERT INTO `student_admin_class` VALUES (6, '20240201001', 3, '2024', 1);
INSERT INTO `student_admin_class` VALUES (7, '20240201002', 3, '2024', 1);
INSERT INTO `student_admin_class` VALUES (8, '20240301001', 1, '2024', 1);
INSERT INTO `student_admin_class` VALUES (9, '20240301002', 1, '2024', 1);
INSERT INTO `student_admin_class` VALUES (10, '20240401001', 2, '2024', 1);
INSERT INTO `student_admin_class` VALUES (11, '20240401002', 2, '2024', 1);
INSERT INTO `student_admin_class` VALUES (12, '20240402001', 3, '2024', 1);
INSERT INTO `student_admin_class` VALUES (13, '20240402002', 3, '2024', 1);
INSERT INTO `student_admin_class` VALUES (14, '20230101001', 4, '2023', 1);
INSERT INTO `student_admin_class` VALUES (15, '20230101002', 4, '2023', 1);
INSERT INTO `student_admin_class` VALUES (16, '20230102001', 5, '2023', 1);
INSERT INTO `student_admin_class` VALUES (17, '20230201001', 5, '2023', 1);
INSERT INTO `student_admin_class` VALUES (18, '20230201002', 5, '2023', 1);
INSERT INTO `student_admin_class` VALUES (19, '20230301001', 4, '2023', 1);
INSERT INTO `student_admin_class` VALUES (20, '20230301002', 4, '2023', 1);
INSERT INTO `student_admin_class` VALUES (21, '20250101001', 9, '2025', 1);

-- ----------------------------
-- Table structure for student_course
-- ----------------------------
DROP TABLE IF EXISTS `student_course`;
CREATE TABLE `student_course`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `student_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `course_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `teaching_class_id` int NULL DEFAULT NULL,
  `academic_year_id` int NULL DEFAULT NULL,
  `score` double NULL DEFAULT NULL COMMENT '成绩',
  `teacher_id` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `teacher_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  `is_makeup` int NULL DEFAULT NULL,
  `original_score` double NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_student_course`(`student_id` ASC, `course_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student_course
-- ----------------------------
INSERT INTO `student_course` VALUES (11, '20240101002', '17', 7, 2, 89, 'T2024004', '刘芳', 1, NULL, NULL);
INSERT INTO `student_course` VALUES (13, '20240101003', '17', 12, 2, 51, 'T2024004', '刘芳', 1, NULL, NULL);
INSERT INTO `student_course` VALUES (14, '20240101001', '17', 7, 2, 84.5, 'T2024004', '刘芳', 1, NULL, NULL);

-- ----------------------------
-- Table structure for system_config
-- ----------------------------
DROP TABLE IF EXISTS `system_config`;
CREATE TABLE `system_config`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `config_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置键',
  `config_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '配置值',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_config_key`(`config_key` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_config
-- ----------------------------
INSERT INTO `system_config` VALUES (1, 'course_selection_enabled', 'true', '选课功能开关：true-开启，false-关闭');
INSERT INTO `system_config` VALUES (2, 'teacher_score_entry_enabled', 'true', '教师录入成绩开关：true-开启，false-关闭');

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `gender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '性别',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色',
  `status` int NULL DEFAULT 0 COMMENT '审核状态：0-待审核，1-已通过，2-已拒绝',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `makeup_exam_permission` tinyint(1) NULL DEFAULT 0 COMMENT '缓考/补考管理权限：true-有，false-无',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '教师信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES (1, 'T2024001', '123456789ok', '张明', 'http://localhost:9090/files/download/1776596101549-0d19b52d7e475569095d0400858eec.jpg', '男', '13912341001', 'TEACHER', 1, 'zhangming@cs.edu.cn', 0);
INSERT INTO `teacher` VALUES (2, 'T2024002', '123456', '李华', NULL, '女', '13912341002', 'TEACHER', 1, 'lihua@cs.edu.cn', 0);
INSERT INTO `teacher` VALUES (3, 'T2024003', '123456', '王强', NULL, '男', '13912341003', 'TEACHER', 1, 'wangqiang@cs.edu.cn', 0);
INSERT INTO `teacher` VALUES (4, 'T2024004', '123456', '刘芳', NULL, '女', '13912341004', 'TEACHER', 1, 'liufang@cs.edu.cn', 1);
INSERT INTO `teacher` VALUES (5, 'T2024005', '123456', '陈明亮', NULL, '男', '13912341005', 'TEACHER', 1, 'chenmingliang@cs.edu.cn', 0);
INSERT INTO `teacher` VALUES (6, 'T2024006', '123456', '赵小红', NULL, '女', '13912341006', 'TEACHER', 1, 'zhaoxiaohong@cs.edu.cn', 0);
INSERT INTO `teacher` VALUES (7, 'T2024007', '123456', '钱建国', NULL, '男', '13912341007', 'TEACHER', 1, 'qianjianguo@cs.edu.cn', 0);
INSERT INTO `teacher` VALUES (8, 'T2024008', '123456', '孙丽', NULL, '女', '13912341008', 'TEACHER', 1, 'sunli@cs.edu.cn', 0);
INSERT INTO `teacher` VALUES (9, 'T2024009', '123456', '周建国', NULL, '男', '13912341009', 'TEACHER', 1, 'zhoujianguo@cs.edu.cn', 0);
INSERT INTO `teacher` VALUES (10, 'T2024010', '123456', '吴敏', NULL, '女', '13912341010', 'TEACHER', 1, 'wumin@cs.edu.cn', 0);
INSERT INTO `teacher` VALUES (11, 'T2024011', '123456', '郑华', NULL, '男', '13912341011', 'TEACHER', 1, 'zhenghua@cs.edu.cn', 0);
INSERT INTO `teacher` VALUES (12, 'T2024012', '123456', '黄丽', NULL, '女', '13912341012', 'TEACHER', 1, 'huangli@cs.edu.cn', 0);
INSERT INTO `teacher` VALUES (13, 'T2024013', '123456', '马建国', NULL, '男', '13912341013', 'TEACHER', 1, 'majiguo@cs.edu.cn', 0);
INSERT INTO `teacher` VALUES (14, 'T2024014', '123456', '朱敏', NULL, '女', '13912341014', 'TEACHER', 1, 'zhumin@cs.edu.cn', 0);
INSERT INTO `teacher` VALUES (15, 'T2024015', '123456', '胡华', NULL, '男', '13912341015', 'TEACHER', 1, 'huhua@cs.edu.cn', 0);
INSERT INTO `teacher` VALUES (16, 'T2024016', '123456', '林丽', NULL, '女', '13912341016', 'TEACHER', 1, 'linli@cs.edu.cn', 0);
INSERT INTO `teacher` VALUES (17, 'T2024017', '123456', '郭建国', NULL, '男', '13912341017', 'TEACHER', 1, 'guojianguo@cs.edu.cn', 0);
INSERT INTO `teacher` VALUES (18, 'T2024018', '123456', '梁敏', NULL, '女', '13912341018', 'TEACHER', 1, 'liangmin@cs.edu.cn', 0);
INSERT INTO `teacher` VALUES (19, 'T2024019', '123456', '谢华', NULL, '男', '13912341019', 'TEACHER', 1, 'xiehua@cs.edu.cn', 0);
INSERT INTO `teacher` VALUES (20, 'T2024020', '123456', '宋丽', 'http://localhost:9090/files/download/1776844315693-0ff7e2e97b44d74ef25052bd7294fb.jpg', '女', '13912341020', 'TEACHER', 1, 'songli@cs.edu.cn', 0);

-- ----------------------------
-- Table structure for teaching_class
-- ----------------------------
DROP TABLE IF EXISTS `teaching_class`;
CREATE TABLE `teaching_class`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `class_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '教学班编号，如 CS101-2024-1-01',
  `course_id` int NOT NULL COMMENT '关联课程ID',
  `academic_year_id` int NOT NULL COMMENT '关联学年学期ID',
  `capacity` int NULL DEFAULT 50 COMMENT '容量',
  `selected_count` int NULL DEFAULT 0 COMMENT '已选人数',
  `location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上课地点',
  `status` int NULL DEFAULT 1 COMMENT '状态：0-未开启，1-已开启，2-已结束',
  `day_of_week` int NULL DEFAULT NULL COMMENT '星期几：1-周一，2-周二...',
  `period_start` int NULL DEFAULT NULL COMMENT '开始节次：1-12',
  `period_end` int NULL DEFAULT NULL COMMENT '结束节次：1-12',
  `teacher_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '主讲教师工号',
  `teacher_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '主讲教师姓名',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_class_code`(`class_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '教学班表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teaching_class
-- ----------------------------
INSERT INTO `teaching_class` VALUES (5, 'CS104-2024-1-01', 11, 1, 45, 0, 'D402', 2, 3, 5, 6, 'T2024016', '林丽');
INSERT INTO `teaching_class` VALUES (6, 'CS403-2024-2-01', 17, 2, 50, 0, 'A208', 1, 1, 3, 4, 'T2024020', '宋丽');
INSERT INTO `teaching_class` VALUES (7, 'CS403-2024-2-02', 17, 2, 51, 2, 'A202', 1, 2, 3, 4, 'T2024004', '刘芳');
INSERT INTO `teaching_class` VALUES (8, 'CS402-2024-2-01', 15, 2, 50, 0, 'A202', 1, 2, 3, 4, 'T2024002', '李华');
INSERT INTO `teaching_class` VALUES (10, 'CS304-2024-2-01', 18, 2, 50, 0, 'E114', 1, 3, 1, 2, 'T2024005', '陈明亮');
INSERT INTO `teaching_class` VALUES (12, 'CS403-2024-2-03', 17, 2, 50, 1, 'A403', 1, 2, 4, 5, 'T2024004', '刘芳');

-- ----------------------------
-- View structure for student_course_view
-- ----------------------------
DROP VIEW IF EXISTS `student_course_view`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `student_course_view` AS select `sc`.`id` AS `id`,`sc`.`student_id` AS `student_id`,`s`.`username` AS `student_username`,`s`.`name` AS `student_name`,`sc`.`course_id` AS `course_id`,`c`.`course_name` AS `course_name`,`sc`.`teaching_class_id` AS `teaching_class_id`,`sc`.`academic_year_id` AS `academic_year_id`,`sc`.`score` AS `score`,`sc`.`teacher_id` AS `teacher_id`,`t`.`username` AS `teacher_username`,`t`.`name` AS `teacher_name`,`sc`.`status` AS `status`,`sc`.`is_makeup` AS `is_makeup`,`sc`.`original_score` AS `original_score` from (((`student_course` `sc` left join `student` `s` on((`sc`.`student_id` = `s`.`username`))) left join `teacher` `t` on((`sc`.`teacher_id` = `t`.`username`))) left join `course` `c` on((`sc`.`course_id` = `c`.`id`)));

-- ----------------------------
-- View structure for student_view
-- ----------------------------
DROP VIEW IF EXISTS `student_view`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `student_view` AS select `student`.`id` AS `id`,`student`.`username` AS `username`,`student`.`username` AS `student_id`,`student`.`password` AS `password`,`student`.`name` AS `name`,`student`.`avatar` AS `avatar`,`student`.`age` AS `age`,`student`.`gender` AS `gender`,`student`.`phone` AS `phone`,`student`.`profession` AS `profession`,`student`.`role` AS `role`,`student`.`status` AS `status`,`student`.`email` AS `email` from `student`;

-- ----------------------------
-- View structure for teacher_view
-- ----------------------------
DROP VIEW IF EXISTS `teacher_view`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `teacher_view` AS select `teacher`.`id` AS `id`,`teacher`.`username` AS `username`,`teacher`.`username` AS `teacher_id`,`teacher`.`password` AS `password`,`teacher`.`name` AS `name`,`teacher`.`avatar` AS `avatar`,`teacher`.`gender` AS `gender`,`teacher`.`phone` AS `phone`,`teacher`.`role` AS `role`,`teacher`.`status` AS `status`,`teacher`.`email` AS `email` from `teacher`;

SET FOREIGN_KEY_CHECKS = 1;
