/*
 Navicat Premium Data Transfer

 Source Server         : host
 Source Server Type    : MySQL
 Source Server Version : 50744 (5.7.44-log)
 Source Host           : localhost:3306
 Source Schema         : sports

 Target Server Type    : MySQL
 Target Server Version : 50744 (5.7.44-log)
 File Encoding         : 65001

 Date: 12/10/2024 11:54:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for academy
-- ----------------------------
DROP TABLE IF EXISTS `academy`;
CREATE TABLE `academy`  (
  `aid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `state` enum('0','1') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`aid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of academy
-- ----------------------------
INSERT INTO `academy` VALUES (1, '软件学院', '1');
INSERT INTO `academy` VALUES (2, '管理学院', '1');
INSERT INTO `academy` VALUES (4, '工程学院', '1');
INSERT INTO `academy` VALUES (5, '马克思学院', '1');
INSERT INTO `academy` VALUES (6, '通识学院', '1');

-- ----------------------------
-- Table structure for arrangement
-- ----------------------------
DROP TABLE IF EXISTS `arrangement`;
CREATE TABLE `arrangement`  (
  `aid` int(11) NOT NULL AUTO_INCREMENT COMMENT '比赛人员安排id',
  `gid` int(11) NULL DEFAULT NULL,
  `uid` int(11) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`aid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of arrangement
-- ----------------------------
INSERT INTO `arrangement` VALUES (3, 2, 1, NULL, 'referee');
INSERT INTO `arrangement` VALUES (4, 2, 2, '红红', 'athlete');
INSERT INTO `arrangement` VALUES (5, 4, 2, '红红', 'athlete');
INSERT INTO `arrangement` VALUES (6, 4, 5, 'hhh', 'athlete');
INSERT INTO `arrangement` VALUES (7, 4, 4, '虎虎', 'referee');
INSERT INTO `arrangement` VALUES (8, 8, 5, 'hhh', 'athlete');
INSERT INTO `arrangement` VALUES (9, 8, 6, 'qqq', 'athlete');
INSERT INTO `arrangement` VALUES (10, 8, 7, 'eee', 'athlete');
INSERT INTO `arrangement` VALUES (11, 8, 9, 'fff', 'athlete');
INSERT INTO `arrangement` VALUES (12, 8, 11, 'ttt', 'athlete');
INSERT INTO `arrangement` VALUES (13, 8, 8, 'aaaa', 'referee');
INSERT INTO `arrangement` VALUES (14, 8, 10, 'ggg', 'referee');
INSERT INTO `arrangement` VALUES (15, 3, 2, '红红', 'athlete');
INSERT INTO `arrangement` VALUES (16, 3, 5, 'hhh', 'athlete');
INSERT INTO `arrangement` VALUES (17, 3, 11, 'ttt', 'athlete');
INSERT INTO `arrangement` VALUES (18, 3, 4, '虎虎', 'referee');
INSERT INTO `arrangement` VALUES (19, 3, 10, 'ggg', 'referee');
INSERT INTO `arrangement` VALUES (24, 13, 5, 'hhh', 'athlete');
INSERT INTO `arrangement` VALUES (25, 13, 11, 'ttt', 'athlete');
INSERT INTO `arrangement` VALUES (26, 13, 4, '虎虎', 'referee');
INSERT INTO `arrangement` VALUES (27, 9, 5, 'hhh', 'athlete');
INSERT INTO `arrangement` VALUES (28, 9, 6, 'qqq', 'athlete');
INSERT INTO `arrangement` VALUES (29, 9, 7, 'eee', 'athlete');
INSERT INTO `arrangement` VALUES (30, 9, 9, 'fff', 'athlete');
INSERT INTO `arrangement` VALUES (31, 9, 11, 'ttt', 'athlete');
INSERT INTO `arrangement` VALUES (32, 9, 8, 'aaaa', 'referee');

-- ----------------------------
-- Table structure for class
-- ----------------------------
DROP TABLE IF EXISTS `class`;
CREATE TABLE `class`  (
  `cid` int(11) NOT NULL AUTO_INCREMENT COMMENT '班级id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '班级名称',
  `state` enum('0','1') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '班级状态',
  PRIMARY KEY (`cid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of class
-- ----------------------------
INSERT INTO `class` VALUES (1, '2021级1班', '1');
INSERT INTO `class` VALUES (2, '2021级2班', '1');
INSERT INTO `class` VALUES (3, '2021级3班', '1');
INSERT INTO `class` VALUES (4, '2021级4班', '1');
INSERT INTO `class` VALUES (6, '2021级5班', '1');
INSERT INTO `class` VALUES (7, '2021级6班', '1');
INSERT INTO `class` VALUES (8, '2021级7班', '1');

-- ----------------------------
-- Table structure for games
-- ----------------------------
DROP TABLE IF EXISTS `games`;
CREATE TABLE `games`  (
  `gid` int(11) NOT NULL AUTO_INCREMENT COMMENT '比赛id',
  `pid` int(11) NULL DEFAULT NULL COMMENT '项目id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `introduce` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '比赛介绍',
  `playerNum` int(11) NULL DEFAULT NULL,
  `refereeNum` int(11) NULL DEFAULT NULL,
  `start_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`gid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of games
-- ----------------------------
INSERT INTO `games` VALUES (1, 1, '第一届春季运动会', '运动', 8, 2, '2024-09-20 19:31:27');
INSERT INTO `games` VALUES (2, 4, '第二届春季运动会', '运动', 10, 1, '2024-09-20 19:31:59');
INSERT INTO `games` VALUES (3, 9, '第三届春季运动会', '运动', 10, 2, '2024-09-23 10:51:38');
INSERT INTO `games` VALUES (4, 6, '短的运动会', '竞技', 5, 2, '2024-09-25 10:21:42');
INSERT INTO `games` VALUES (8, 5, 'test', '11', 12, 2, '2024-09-25 17:01:43');
INSERT INTO `games` VALUES (9, 2, '第一届秋季运动会', '赛出风采', 6, 1, '2024-09-27 16:48:52');
INSERT INTO `games` VALUES (10, 5, '第二届秋季运动会', '友谊第一', 12, 2, '2024-09-27 16:49:30');
INSERT INTO `games` VALUES (13, 1, '22', '22', 8, 2, '2024-09-30 10:41:56');

-- ----------------------------
-- Table structure for persistent_logins
-- ----------------------------
DROP TABLE IF EXISTS `persistent_logins`;
CREATE TABLE `persistent_logins`  (
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `series` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `token` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `last_used` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`series`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of persistent_logins
-- ----------------------------

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project`  (
  `pid` int(11) NOT NULL AUTO_INCREMENT COMMENT '项目id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `playerNum` int(11) NULL DEFAULT NULL COMMENT '运动员人数',
  `refereeNum` int(11) NULL DEFAULT NULL COMMENT '裁判员人数',
  `state` enum('0','1') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1',
  PRIMARY KEY (`pid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of project
-- ----------------------------
INSERT INTO `project` VALUES (1, '100米短跑', 8, 2, '1');
INSERT INTO `project` VALUES (2, '50米短跑', 6, 1, '1');
INSERT INTO `project` VALUES (3, '1000米长跑', 18, 3, '0');
INSERT INTO `project` VALUES (4, '跳高', 10, 1, '1');
INSERT INTO `project` VALUES (5, '跳远', 12, 2, '1');
INSERT INTO `project` VALUES (6, '接力跑', 5, 2, '1');
INSERT INTO `project` VALUES (7, '拔河', 20, 1, '1');
INSERT INTO `project` VALUES (9, '5000米长跑', 10, 2, '1');

-- ----------------------------
-- Table structure for registration
-- ----------------------------
DROP TABLE IF EXISTS `registration`;
CREATE TABLE `registration`  (
  `rid` int(11) NOT NULL AUTO_INCREMENT COMMENT '报名id',
  `uid` int(11) NULL DEFAULT NULL,
  `gid` int(11) NULL DEFAULT NULL,
  `role` enum('athlete','referee') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `state` enum('0','1','2') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '审核状态',
  PRIMARY KEY (`rid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of registration
-- ----------------------------
INSERT INTO `registration` VALUES (1, 1, 2, 'athlete', '1');
INSERT INTO `registration` VALUES (2, 2, 2, 'referee', '1');
INSERT INTO `registration` VALUES (3, 2, 4, 'athlete', '1');
INSERT INTO `registration` VALUES (4, 2, 3, 'athlete', '2');
INSERT INTO `registration` VALUES (5, 2, 1, 'athlete', '1');
INSERT INTO `registration` VALUES (6, 4, 4, 'referee', '1');
INSERT INTO `registration` VALUES (7, 4, 3, 'referee', '1');
INSERT INTO `registration` VALUES (8, 5, 4, 'athlete', '1');
INSERT INTO `registration` VALUES (9, 5, 8, 'athlete', '1');
INSERT INTO `registration` VALUES (11, 5, 3, 'athlete', '1');
INSERT INTO `registration` VALUES (12, 5, 9, 'athlete', '1');
INSERT INTO `registration` VALUES (13, 6, 8, 'athlete', '1');
INSERT INTO `registration` VALUES (14, 6, 9, 'athlete', '1');
INSERT INTO `registration` VALUES (15, 6, 10, 'athlete', '0');
INSERT INTO `registration` VALUES (16, 7, 8, 'athlete', '1');
INSERT INTO `registration` VALUES (17, 7, 9, 'athlete', '1');
INSERT INTO `registration` VALUES (18, 7, 1, 'athlete', '0');
INSERT INTO `registration` VALUES (19, 8, 8, 'referee', '1');
INSERT INTO `registration` VALUES (20, 8, 1, 'referee', '0');
INSERT INTO `registration` VALUES (21, 8, 9, 'referee', '1');
INSERT INTO `registration` VALUES (22, 9, 8, 'athlete', '1');
INSERT INTO `registration` VALUES (23, 9, 1, 'athlete', '0');
INSERT INTO `registration` VALUES (24, 9, 9, 'athlete', '1');
INSERT INTO `registration` VALUES (25, 10, 8, 'referee', '1');
INSERT INTO `registration` VALUES (26, 10, 3, 'referee', '1');
INSERT INTO `registration` VALUES (27, 10, 1, 'referee', '0');
INSERT INTO `registration` VALUES (28, 11, 8, 'athlete', '1');
INSERT INTO `registration` VALUES (29, 11, 9, 'athlete', '1');
INSERT INTO `registration` VALUES (30, 11, 3, 'athlete', '1');
INSERT INTO `registration` VALUES (32, 11, 1, 'athlete', '0');
INSERT INTO `registration` VALUES (37, 5, 13, 'athlete', '1');
INSERT INTO `registration` VALUES (38, 11, 13, 'athlete', '1');
INSERT INTO `registration` VALUES (39, 4, 13, 'referee', '1');
INSERT INTO `registration` VALUES (40, 11, 10, 'athlete', '0');

-- ----------------------------
-- Table structure for score_detail
-- ----------------------------
DROP TABLE IF EXISTS `score_detail`;
CREATE TABLE `score_detail`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sid` int(11) NULL DEFAULT NULL,
  `uid` int(11) NULL DEFAULT NULL,
  `score` double NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of score_detail
-- ----------------------------
INSERT INTO `score_detail` VALUES (1, 1, 2, 85.5);
INSERT INTO `score_detail` VALUES (2, 1, 5, 92.5);
INSERT INTO `score_detail` VALUES (4, 3, 5, 77);
INSERT INTO `score_detail` VALUES (5, 3, 6, 78.5);
INSERT INTO `score_detail` VALUES (6, 3, 7, 90);
INSERT INTO `score_detail` VALUES (7, 3, 9, 92.5);
INSERT INTO `score_detail` VALUES (8, 3, 11, 88.5);
INSERT INTO `score_detail` VALUES (9, 4, 2, 86);
INSERT INTO `score_detail` VALUES (10, 4, 5, 79);
INSERT INTO `score_detail` VALUES (11, 4, 11, 85);
INSERT INTO `score_detail` VALUES (15, 6, 5, 68);
INSERT INTO `score_detail` VALUES (16, 6, 11, 78);
INSERT INTO `score_detail` VALUES (17, 7, 5, 98.5);
INSERT INTO `score_detail` VALUES (18, 7, 6, 88);
INSERT INTO `score_detail` VALUES (19, 7, 7, 92.5);
INSERT INTO `score_detail` VALUES (20, 7, 9, 96);
INSERT INTO `score_detail` VALUES (21, 7, 11, 86);

-- ----------------------------
-- Table structure for score_list
-- ----------------------------
DROP TABLE IF EXISTS `score_list`;
CREATE TABLE `score_list`  (
  `sid` int(11) NOT NULL AUTO_INCREMENT COMMENT '成绩id',
  `gid` int(11) NULL DEFAULT NULL,
  `info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `state` enum('0','1') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '成绩审核状态',
  PRIMARY KEY (`sid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of score_list
-- ----------------------------
INSERT INTO `score_list` VALUES (1, 4, NULL, '1');
INSERT INTO `score_list` VALUES (3, 8, NULL, '1');
INSERT INTO `score_list` VALUES (4, 3, NULL, '0');
INSERT INTO `score_list` VALUES (6, 13, NULL, '1');
INSERT INTO `score_list` VALUES (7, 9, NULL, '1');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `uid` int(11) NOT NULL AUTO_INCREMENT,
  `aid` int(11) NULL DEFAULT NULL COMMENT '所属学院id',
  `cid` int(11) NULL DEFAULT NULL COMMENT '所属班级id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role` enum('admin','student','teacher') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色/身份',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮箱地址',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`uid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, NULL, NULL, 'admin', 'admin', '$2a$10$55F/8TH2rIfUyNuP3rPKNOmNzNXhvuHnkKoQG6YBwCvT0Fp2I1lGK', 'admin', '222', NULL, '2024-09-14 18:35:31', '2024-09-25 17:15:19');
INSERT INTO `users` VALUES (2, 1, 3, '红红', 'feet', '$2a$10$Au3tqG3nT0rPQo0ZmhIJ1OEvT63JcATgbOsiinJElT66.whcthqom', 'student', '111', NULL, '2024-09-18 14:13:35', '2024-09-26 08:49:54');
INSERT INTO `users` VALUES (4, 2, 2, '虎虎', 'teacher', '$2a$10$55F/8TH2rIfUyNuP3rPKNOmNzNXhvuHnkKoQG6YBwCvT0Fp2I1lGK', 'teacher', '333', NULL, '2024-09-25 14:32:37', '2024-09-27 10:27:42');
INSERT INTO `users` VALUES (5, 1, 3, 'hhh', 'hhh', '$2a$10$vkWneHSHFMNMvgrHOHYDHuMHDJPgWjGhiqhIwJcY41pEdGrOj8SKu', 'student', '3107907150@qq.com', NULL, '2024-09-26 08:53:28', '2024-09-26 08:57:23');
INSERT INTO `users` VALUES (6, 2, 3, 'qqq', 'qqq', '$2a$10$vkWneHSHFMNMvgrHOHYDHuMHDJPgWjGhiqhIwJcY41pEdGrOj8SKu', 'student', '33', NULL, '2024-09-27 10:24:00', '2024-09-27 10:25:24');
INSERT INTO `users` VALUES (7, 2, 3, 'eee', 'eee', '$2a$10$vkWneHSHFMNMvgrHOHYDHuMHDJPgWjGhiqhIwJcY41pEdGrOj8SKu', 'student', '555', NULL, '2024-09-27 10:24:19', '2024-09-27 10:25:29');
INSERT INTO `users` VALUES (8, 5, 6, 'aaaa', 'aaa', '$2a$10$vkWneHSHFMNMvgrHOHYDHuMHDJPgWjGhiqhIwJcY41pEdGrOj8SKu', 'teacher', '666', NULL, '2024-09-27 10:24:37', '2024-09-27 10:25:36');
INSERT INTO `users` VALUES (9, 1, 4, 'fff', 'fff', '$2a$10$vkWneHSHFMNMvgrHOHYDHuMHDJPgWjGhiqhIwJcY41pEdGrOj8SKu', 'student', '555', NULL, '2024-09-27 10:24:52', '2024-09-27 10:25:44');
INSERT INTO `users` VALUES (10, 1, 4, 'ggg', 'ggg', '$2a$10$vkWneHSHFMNMvgrHOHYDHuMHDJPgWjGhiqhIwJcY41pEdGrOj8SKu', 'teacher', 'ggg', NULL, '2024-09-27 10:25:06', '2024-09-27 10:25:49');
INSERT INTO `users` VALUES (11, 1, 3, 'ttt', 'ttt', '$2a$10$vkWneHSHFMNMvgrHOHYDHuMHDJPgWjGhiqhIwJcY41pEdGrOj8SKu', 'student', 'ttt', NULL, '2024-09-27 10:29:12', '2024-09-27 10:29:39');
INSERT INTO `users` VALUES (12, 2, 3, 'iin', 'iin', '$2a$10$vkWneHSHFMNMvgrHOHYDHuMHDJPgWjGhiqhIwJcY41pEdGrOj8SKu', 'student', 's', NULL, '2024-09-28 15:52:15', '2024-09-28 15:52:37');

SET FOREIGN_KEY_CHECKS = 1;
