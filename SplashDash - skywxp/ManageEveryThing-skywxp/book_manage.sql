/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : localhost:3306
 Source Schema         : book_manage

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 28/08/2022 14:37:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book`  (
  `bid` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `descrp` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `price` decimal(10, 2) NULL DEFAULT NULL,
  PRIMARY KEY (`bid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book
-- ----------------------------
INSERT INTO `book` VALUES (9, '老人与海', '讲述一位老人与大海斗争的故事', 10.00);
INSERT INTO `book` VALUES (10, '澎湖湾', '歌曲', 10.00);
INSERT INTO `book` VALUES (11, '测试书籍300', '无', 1000.00);
INSERT INTO `book` VALUES (12, '哈哈哈哈哈哈', '132123', 123.00);
INSERT INTO `book` VALUES (14, '哈哈哈哈哈哈哈哈哈', '123', 123.00);
INSERT INTO `book` VALUES (15, '123123123', '123123123', 1230.00);

-- ----------------------------
-- Table structure for borrow
-- ----------------------------
DROP TABLE IF EXISTS `borrow`;
CREATE TABLE `borrow`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `bid` int NULL DEFAULT NULL,
  `sid` int NULL DEFAULT NULL,
  `time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of borrow
-- ----------------------------
INSERT INTO `borrow` VALUES (10, 9, 7, '2022-03-07 15:19:07');
INSERT INTO `borrow` VALUES (11, 10, 7, '2022-03-07 15:19:09');
INSERT INTO `borrow` VALUES (13, 11, 4, '2022-08-28 13:13:39');

-- ----------------------------
-- Table structure for persistent_logins
-- ----------------------------
DROP TABLE IF EXISTS `persistent_logins`;
CREATE TABLE `persistent_logins`  (
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `series` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `token` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `last_used` timestamp NOT NULL,
  PRIMARY KEY (`series`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of persistent_logins
-- ----------------------------

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `sid` int NOT NULL AUTO_INCREMENT,
  `uid` int NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sex` enum('男','女') CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '男',
  `grade` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`sid`) USING BTREE,
  INDEX `f_uid`(`uid`) USING BTREE,
  CONSTRAINT `f_uid` FOREIGN KEY (`uid`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES (1, 5, 'test1', '男', '2019');
INSERT INTO `student` VALUES (2, 7, 'test2', '男', '2019');
INSERT INTO `student` VALUES (3, 8, 'test3', '男', '2019');
INSERT INTO `student` VALUES (4, 9, 'test', '男', '2019');
INSERT INTO `student` VALUES (5, 11, '小刚', '男', '2019');
INSERT INTO `student` VALUES (6, 12, '小红', '男', '2019');
INSERT INTO `student` VALUES (7, 13, '小明', '男', '2019');
INSERT INTO `student` VALUES (8, 14, '小王', '男', '2019');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `role` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, '图书管理员', 'admin', '$2a$10$OP8zqGHW/zq5ULtkNgwfKuREd0aHgZorNDehvzjlix5xo19SpPS1S');
INSERT INTO `users` VALUES (5, 'test1', 'user', '$2a$10$H3ix6nkIG1IRzwbQUYy8zeJ9qQbVPIJz2BXVNMEhgs/BRQjXNA336');
INSERT INTO `users` VALUES (7, 'test2', 'user', '$2a$10$7QKKZua4j6GoBwUMAEEQt.BQi660yCBqwgWlY5ZYfe8kZb5f39Pmi');
INSERT INTO `users` VALUES (8, 'test3', 'user', '$2a$10$UO/taT4.LgrNmRGMW.pa..WO9H4rH2AGo0fqql/DmlVvpm4hSSu1i');
INSERT INTO `users` VALUES (9, 'test', 'user', '$2a$10$CWl/uNx2homXkoztW.irW.tmYjlOBzAYicmMt2Lr3ggAQY2VM2sly');
INSERT INTO `users` VALUES (11, '小刚', 'user', '$2a$10$.uxcMn5ONfKxUZlR//LhVO6eAdMkGZwg0fBGnCOyNxmkHR6WgG.X.');
INSERT INTO `users` VALUES (12, '小红', 'user', '$2a$10$JLsLu2AuKQJP3vSeMXD25.C9GnEqozlPEAnnFcLqtOOLoHzKFEuwK');
INSERT INTO `users` VALUES (13, '小明', 'user', '$2a$10$rx5vSb88/Ox7q3p9gps4.O1XkpjuyHdFN/R9KyquMDucg4Manekii');
INSERT INTO `users` VALUES (14, '小王', 'user', '$2a$10$OP8zqGHW/zq5ULtkNgwfKuREd0aHgZorNDehvzjlix5xo19SpPS1S');

-- ----------------------------
-- Triggers structure for table book
-- ----------------------------
DROP TRIGGER IF EXISTS `del_book`;
delimiter ;;
CREATE TRIGGER `del_book` BEFORE DELETE ON `book` FOR EACH ROW DELETE from borrow where bid = old.bid
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
