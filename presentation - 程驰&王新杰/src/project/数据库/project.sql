/*
MySQL Data Transfer
Source Host: localhost
Source Database: project
Target Host: localhost
Target Database: project
Date: 2021/12/13 17:40:07
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for file
-- ----------------------------
CREATE TABLE `file` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `url` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `name` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for money
-- ----------------------------
CREATE TABLE `money` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `pid` int(8) DEFAULT NULL,
  `money` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `givedate` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `purpose` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for project
-- ----------------------------
CREATE TABLE `project` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `header` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `tel` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `college` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `participant` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `teacher` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `teachertel` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `teachertitle` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `unit` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `signdate` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `fileurl` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `verify` varchar(5) CHARACTER SET utf8 DEFAULT NULL,
  `money` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `givedate` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `purpose` varchar(200) CHARACTER SET utf8 DEFAULT NULL,
  `checkinfo` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `evaluate` varchar(200) CHARACTER SET utf8 DEFAULT NULL,
  `checkdate` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `uid` int(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for student
-- ----------------------------
CREATE TABLE `student` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `realname` varchar(50) DEFAULT NULL,
  `tel` varchar(50) DEFAULT NULL,
  `college` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for users
-- ----------------------------
CREATE TABLE `users` (
  `uid` int(8) NOT NULL AUTO_INCREMENT,
  `uname` varchar(50) DEFAULT NULL,
  `urealname` varchar(50) DEFAULT NULL,
  `upassword` varchar(50) DEFAULT NULL,
  `utel` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `file` VALUES ('2', '/upload/1492134136488.doc', '项目申报模板');
INSERT INTO `file` VALUES ('3', '/upload/1492134502793.doc', '申请表');
INSERT INTO `money` VALUES ('2', '2', '4000', '2021-04-12', '资金用途资金用途资金用途');
INSERT INTO `money` VALUES ('3', '2', '40001', '2021-04-12', '资金用途资金用途资金用途');
INSERT INTO `money` VALUES ('4', '3', '15000', '2021-03-04', '购买硬件');
INSERT INTO `money` VALUES ('5', '3', '5000', '2021-03-23', '办公用品');
INSERT INTO `money` VALUES ('6', '4', '5000', '2021-03-20', '购买办公用品');
INSERT INTO `money` VALUES ('7', '4', '3500', '2021-04-14', '企业调研');
INSERT INTO `project` VALUES ('2', '大数据研究', '李小明', '13511220011', '计算机学院', '王明，赵利', '李旺', '13844541152', '教授', '计算机学院', '2021-08-08', '/upload/js.js', '1', '4000', '2021-04-12', '启动资金', '优秀', '评价情况90分，整体挺好', '2021-03-22', '10');
INSERT INTO `project` VALUES ('3', '云端数据存储', '王月', '13741544153', '信息学院', '赵亮，张丽', '李鹏', '13541225222', '教授', '计算机学院', '2021-04-06', '/upload/1492062264901.doc', '1', null, null, null, '合格', '80分，整体还行，需要改进', '2021-03-13', '12');
INSERT INTO `project` VALUES ('4', '企业拓扑研究', '张亮', '13520213320', '计算机学院', '李伟，赵龙', '张明', '13854121141', '教授', '计算机教研室', '2021-04-14', '/upload/1492134418360.doc', '1', null, null, null, '合格', '项目做得很好', '2021-03-14', '13');
INSERT INTO `project` VALUES ('5', '大数据安全', '于小明', '13874115524', '计算机学院', '王月，李浩', '张伟 ', '13744124452', '教授', '计算机学院', '2021-03-22', '/upload/1553389585135.rar', '0', null, null, null, null, null, null, '12');
INSERT INTO `student` VALUES ('10', '20170012', 'student', '王明明', '13511412256', '信息学院', '123456');
INSERT INTO `student` VALUES ('11', '20170414', 'student1', '张小伟', '13522103320', '信息学院', '123456');
INSERT INTO `student` VALUES ('12', '20170502', 'lnyxb', '李明', '13520213320', '计算机学院', '123456');
INSERT INTO `student` VALUES ('13', '20170412', 'zhangliang', '张亮', '13520213320', '计算机学院', '123456');
INSERT INTO `users` VALUES ('1', 'admin', '管理员', 'admin', '13789089088');
INSERT INTO `users` VALUES ('2', 'liwei', '王亮', '1', '13565203320');
