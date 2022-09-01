/*
SQLyog Ultimate v10.00 Beta1
MySQL - 5.7.37 : Database - ouni
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`ouni` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `ouni`;

/*Table structure for table `chat_record` */

DROP TABLE IF EXISTS `chat_record`;

CREATE TABLE `chat_record` (
  `records_id` int(10) NOT NULL AUTO_INCREMENT,
  `from_user_id` int(10) DEFAULT NULL,
  `to_user_id` int(10) DEFAULT NULL,
  `record_time` datetime DEFAULT NULL,
  `message` varchar(100) DEFAULT NULL,
  `img_url` varchar(200) DEFAULT NULL,
  `status` tinyint(1) DEFAULT '0' COMMENT '0未读，1已读',
  PRIMARY KEY (`records_id`)
) ENGINE=InnoDB AUTO_INCREMENT=431 DEFAULT CHARSET=utf8mb4;

/*Data for the table `chat_record` */

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `user_id` int(10) NOT NULL AUTO_INCREMENT,
  `open_id` varchar(50) NOT NULL,
  `user_name` varchar(50) DEFAULT NULL,
  `img_url` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4;

/*Data for the table `user` */

insert  into `user`(`user_id`,`open_id`,`user_name`,`img_url`) values (3,'oSdjC5YXIG1qkUo5Xp0UlfKBZC4s','Embrace','https://thirdwx.qlogo.cn/mmopen/vi_32/LPic6DqtdWKMjpLEq2bNWSOKF6xmSuf2d5649mUUWNfh7JIiaiaAHzYGPuTesL9kHxrFibOK03nOPVsNiaYribOjLJcw/132'),(4,'oSdjC5SK-WQqs8VHYzPLZyUo9u_g','一颗心的距离','https://thirdwx.qlogo.cn/mmopen/vi_32/pOAP88IVhtXr9iazHgtEQdqM3ibbRLVo4aoJ0w2O6oHjIfVV3KEgGGaichKMd3Z2VCzSQf5AWicd8UaMbiakSVY76Pw/132'),(5,'1111','飞翔的企鹅','https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg2.niutuku.com%2Fdesk%2F1208%2F0728%2Fbizhi-0728-9516.jpg&refer=http%3A%2F%2Fimg2.niutuku.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?'),(6,'222','漫游者','https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F202005%2F02%2F20200502185802_FuFU2.thumb.1000_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999'),(7,'33','兰亭序','https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201810%2F05%2F20181005203850_cAAYa.thumb.1000_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
