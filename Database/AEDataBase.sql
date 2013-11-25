SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT;
SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS;
SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION;
SET NAMES utf8;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE=NO_AUTO_VALUE_ON_ZERO */;


CREATE DATABASE /*!32312 IF NOT EXISTS*/ `assessmentengine`;
USE `assessmentengine`;
CREATE TABLE `admins` (
  `seq` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(250) DEFAULT NULL,
  `email` varchar(250) DEFAULT NULL,
  `mobile` varchar(250) DEFAULT NULL,
  `city` varchar(250) DEFAULT NULL,
  `username` varchar(250) DEFAULT NULL,
  `password` varchar(250) DEFAULT NULL,
  `isenable` tinyint(4) DEFAULT NULL,
  `issuperuser` tinyint(4) DEFAULT NULL,
  `createdon` datetime DEFAULT NULL,
  `lastmodifieddate` datetime DEFAULT NULL,
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=latin1;
INSERT INTO `admins` (`seq`,`name`,`email`,`mobile`,`city`,`username`,`password`,`isenable`,`issuperuser`,`createdon`,`lastmodifieddate`) VALUES (1,'baljeetss','baljeetgaheesa@gmail.com','9417511','ldhsaa','baljeet','b',1,1,'2013-07-18 21:51:53','2013-08-13 22:40:11'),(2,'','','','','','',0,0,'2013-08-03 13:23:16',NULL),(3,'baljeet','baljeetgaheer@gmail.com','12345','ct','ads','ss',0,0,'2013-08-11 22:22:21',NULL),(4,'ghghgh','baljeetgaheer@gmail.com','ghgg','ghgh','ghj','ss',0,0,'2013-08-13 22:16:58','2013-08-13 22:16:58'),(5,'baljeets','baljeetgaheers@gmail.com','94175s','ldhs',NULL,NULL,0,0,'2013-08-13 22:20:00','2013-08-13 22:20:00'),(6,'bb','baljeetgaheer@gmail.com','12345','as','adba','ss',0,0,'2013-08-24 22:22:47','2013-08-24 22:22:47'),(7,'bb','baljeetgaheer@gmail.com','12345','as','adbab','ss',0,0,'2013-08-24 22:23:53','2013-08-24 22:23:53'),(8,'bb','baljeetgaheer@gmail.com','12345','as','adbaba','ss',0,0,'2013-08-24 22:25:52','2013-08-24 22:25:52'),(9,'bb','baljeetgaheer@gmail.com','12345','as','adbabaz','ss',0,0,'2013-08-24 22:27:27','2013-08-24 22:27:27'),(10,'bb','baljeetgaheer@gmail.com','12345','as','adbabazs','ss',0,0,'2013-08-24 22:30:33','2013-08-24 22:30:33');
INSERT INTO `admins` (`seq`,`name`,`email`,`mobile`,`city`,`username`,`password`,`isenable`,`issuperuser`,`createdon`,`lastmodifieddate`) VALUES (11,'bdd','baljeetgaheer@gmail.com','123455','asdfa','baljeetgaheer1234','ss',0,0,'2013-08-26 22:20:28','2013-08-26 22:20:28'),(12,'ss','baljeetgaheer@gmail.com','12345','ssd','ss','ss',0,0,'2013-08-26 22:26:29','2013-08-26 22:26:29'),(13,'asdfgh','baljeetgaheer@gmail.com','12345','cvb','asd','ss',0,0,'2013-08-26 22:34:00','2013-08-26 22:34:00'),(14,'baljeetss','baljeetgaheer@gmail.com','sasfasdf','asss','ssser','dd',0,0,'2013-08-26 22:55:36','2013-08-26 22:55:36'),(15,'Baljeet Singh Gaheer','baljeetgaheer@gmail.com','9856585569','Ludhiana','bir','dd',0,0,'2013-08-28 21:35:07','2013-08-28 21:35:07'),(16,'Baljeet Singh Gaheer','baljeetgaheer@gmail.com','19283839403','ludhiana','birsing','ss',0,0,'2013-08-28 21:39:04','2013-08-28 21:39:04'),(17,'Baljeet Singh Gaheer','baljeetgaheer@gmail.com','19283839403','ludhiana','birsing1','ss',0,0,'2013-08-28 21:46:59','2013-08-28 21:46:59'),(18,'Baljeet Singh Gaheer','baljeetgaheer@gmail.com','19283839403','ludhiana','birsing12','dd',0,0,'2013-08-28 21:48:39','2013-08-28 21:48:39');
INSERT INTO `admins` (`seq`,`name`,`email`,`mobile`,`city`,`username`,`password`,`isenable`,`issuperuser`,`createdon`,`lastmodifieddate`) VALUES (19,'Baljeet Singh Gaheer','baljeetgaheer@gmail.com','19283839403','ludhiana','birsing123','ss',0,0,'2013-08-28 21:50:44','2013-08-28 21:50:44'),(20,'Baljeet Singh Gaheer','baljeetgaheer@gmail.com','19283839403','ludhiana','birsing1234','ss',0,0,'2013-08-28 21:58:09','2013-08-28 21:58:09'),(21,'Baljeet Singh Gaheer','baljeetgaheer@gmail.com','19283839403','ludhiana','birsing12345','ss',0,0,'2013-08-28 22:00:52','2013-08-28 22:00:52'),(22,'Baljeet Singh Gaheer','baljeetgaheer@gmail.com','19283839403','adf','basdfg','ssd',0,0,'2013-08-29 21:11:08','2013-08-29 21:11:08'),(23,'asdf','baljeetgaheer@gmail.com','123456','asdf','uu','ss',0,0,'2013-08-29 21:21:59','2013-08-29 21:21:59'),(24,'asdf','baljeetgaheer@gmail.com','111','dfg','ssasdf','aa',0,0,'2013-08-29 21:25:01','2013-08-29 21:25:01'),(25,'Baljeet Singh Gaheer','baljeetgaheer@gmail.com','123456','asdfg','qwerty','asd',0,0,'2013-08-29 21:27:26','2013-08-29 21:27:26'),(26,'Baljeet Singh Gaheer','baljeetgaheer@gmail.com','19283839403','cvb','bajrrt','ss',0,0,'2013-08-29 21:29:44','2013-08-29 21:29:44');
INSERT INTO `admins` (`seq`,`name`,`email`,`mobile`,`city`,`username`,`password`,`isenable`,`issuperuser`,`createdon`,`lastmodifieddate`) VALUES (27,'Baljeet Singh Gaheer','baljeetgaheer@gmail.com','1234567','cvbnm,','ssp','ss',0,0,'2013-08-29 21:35:12','2013-08-29 21:35:12'),(28,'Baljeet Singh Gaheer','baljeetgaheer@gmail.com','1235','asdaf','bn','ss',0,0,'2013-08-29 21:39:07','2013-08-29 21:39:07'),(29,'Baljeet Singh Gaheer','baljeetgaheer@gmail.com','1235','asdaf','BaljeetSingh','bb',0,0,'2013-08-29 21:42:33','2013-08-29 21:42:33');
CREATE TABLE `campaigngames` (
  `seq` int(11) NOT NULL AUTO_INCREMENT,
  `gameseq` int(11) NOT NULL,
  `campaignseq` int(11) NOT NULL,
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;
INSERT INTO `campaigngames` (`seq`,`gameseq`,`campaignseq`) VALUES (2,14,20),(13,17,4),(14,15,21);
CREATE TABLE `campaigns` (
  `seq` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(250) DEFAULT NULL,
  `description` varchar(250) DEFAULT NULL,
  `createdon` datetime DEFAULT NULL,
  `validitydays` int(11) DEFAULT NULL,
  `isenabled` tinyint(4) DEFAULT NULL,
  `projectseq` int(11) DEFAULT NULL,
  `lastmodifieddate` datetime DEFAULT NULL,
  `validtilldate` datetime DEFAULT NULL,
  `startdate` datetime DEFAULT NULL,
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=latin1;
INSERT INTO `campaigns` (`seq`,`name`,`description`,`createdon`,`validitydays`,`isenabled`,`projectseq`,`lastmodifieddate`,`validtilldate`,`startdate`) VALUES (4,'dessasgag','desyyddd','2013-10-24 22:12:27',NULL,1,31,'2013-11-23 15:26:50',NULL,NULL),(14,'New Campaign','Desc','2013-10-24 21:25:35',NULL,1,31,'2013-10-24 21:25:35',NULL,NULL),(16,'New Summer Campaign','Des ','2013-10-24 22:46:43',NULL,1,31,'2013-10-24 22:46:43',NULL,NULL),(17,'New Campaign wi','e','2013-10-24 22:48:58',NULL,1,31,'2013-10-24 22:48:58',NULL,NULL),(18,'test smn','des','2013-10-24 22:58:41',NULL,1,31,'2013-10-24 22:58:41',NULL,NULL),(19,'summy campaign','des','2013-10-24 23:15:48',NULL,1,31,'2013-10-24 23:15:48',NULL,NULL),(20,'test 1234','de','2013-10-24 23:20:48',NULL,1,31,'2013-10-24 23:20:48',NULL,NULL),(21,'camapaigne','description','2013-10-25 15:35:23',NULL,1,31,'2013-11-23 15:43:24',NULL,NULL),(22,'test','tt','2013-11-19 21:45:28',NULL,0,31,'2013-11-19 21:45:28',NULL,NULL);
CREATE TABLE `campaignsets` (
  `seq` int(11) NOT NULL AUTO_INCREMENT,
  `campaignseq` int(11) DEFAULT NULL,
  `setseq` int(11) DEFAULT NULL,
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
CREATE TABLE `campaignusergroups` (
  `seq` int(11) NOT NULL AUTO_INCREMENT,
  `campaignseq` int(11) DEFAULT NULL,
  `usergroupseq` int(11) DEFAULT NULL,
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=latin1;
INSERT INTO `campaignusergroups` (`seq`,`campaignseq`,`usergroupseq`) VALUES (1,80,4),(16,4,3),(17,21,27);
CREATE TABLE `configuration` (
  `key` varchar(200) NOT NULL DEFAULT '',
  `value` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
CREATE TABLE `gamequestions` (
  `seq` int(11) NOT NULL AUTO_INCREMENT,
  `gameseq` int(11) DEFAULT NULL,
  `questionseq` int(11) DEFAULT NULL,
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=98 DEFAULT CHARSET=latin1;
INSERT INTO `gamequestions` (`seq`,`gameseq`,`questionseq`) VALUES (4,4,1),(5,384,75),(6,5,76),(8,6,80),(9,6,81),(10,7,94),(11,8,110),(12,620,132),(13,620,133),(14,620,134),(15,636,136),(16,636,137),(17,636,138),(34,9,143),(35,9,142),(36,9,144),(37,9,145),(38,9,146),(39,9,147),(40,9,148),(41,9,149),(42,10,150),(43,11,151),(44,12,152),(48,13,153),(49,13,154),(50,13,155),(51,13,156),(52,13,157),(53,13,158),(55,14,159),(56,14,160),(57,14,161),(58,14,162),(67,16,171),(68,16,172),(88,17,177),(89,15,175),(90,15,174),(91,15,173),(92,15,163),(93,15,176),(94,15,166),(95,15,165),(96,15,164),(97,15,178);
CREATE TABLE `games` (
  `seq` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(200) CHARACTER SET utf8 DEFAULT NULL,
  `description` varchar(250) DEFAULT NULL,
  `isenabled` tinyint(4) DEFAULT NULL,
  `lastmodifieddate` datetime DEFAULT NULL,
  `gametemplateseq` int(11) DEFAULT NULL,
  `projectseq` int(11) DEFAULT NULL,
  `createdon` datetime DEFAULT NULL,
  `maxsecondsallowed` int(11) DEFAULT NULL,
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=latin1;
INSERT INTO `games` (`seq`,`title`,`description`,`isenabled`,`lastmodifieddate`,`gametemplateseq`,`projectseq`,`createdon`,`maxsecondsallowed`) VALUES (10,'Template1','desc',1,NULL,1,31,NULL,0),(11,'Template1','desc',1,NULL,1,31,NULL,0),(12,'Template1','desc',1,NULL,1,31,NULL,0),(13,'Template1','desc',1,'2013-10-24 00:00:00',1,31,'2013-10-24 23:16:33',0),(14,'Template1','desc',1,NULL,1,31,NULL,0),(15,'Template1','desc',1,NULL,1,0,NULL,0),(16,'Template1','desc',1,NULL,1,31,NULL,0),(17,'test game','desc',1,NULL,1,0,NULL,0);
CREATE TABLE `gametemplates` (
  `seq` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `path` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `imagepath` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `description` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
INSERT INTO `gametemplates` (`seq`,`name`,`path`,`imagepath`,`description`) VALUES (1,'Template1','test image path','test path','desc'),(2,'Tempalte 2 ','test image path 2 ','test path 2 ','desc2');
CREATE TABLE `projects` (
  `seq` int(11) NOT NULL AUTO_INCREMENT,
  `adminseq` int(11) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `contactperson` varchar(250) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `address` varchar(500) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `country` varchar(50) DEFAULT NULL,
  `createdon` datetime DEFAULT NULL,
  `lastmodifieddate` datetime DEFAULT NULL,
  `isenabled` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=latin1;
INSERT INTO `projects` (`seq`,`adminseq`,`name`,`description`,`email`,`phone`,`contactperson`,`mobile`,`address`,`city`,`country`,`createdon`,`lastmodifieddate`,`isenabled`) VALUES (31,1,'p4s','dess2hs','baljeetgaheer@gmail.co','12313sdd','asgasggss','gsagag','addd','c','dds','2013-08-02 23:17:29','2013-09-26 21:28:57',1),(32,1,'test 12345a','desc','baljeetgaheer@gmail.com','12345s','asdf','ada','asdf','ff','ff','2013-08-02 23:18:44','2013-09-25 22:03:06',1),(33,1,'p5','desc','baljeetgaheer@gmail.com','1235','asdf','asdf','asdf','asdf','asdf','2013-08-02 23:21:25','2013-08-02 23:21:42',1),(34,0,'34','asa','baljeetgaheer@gmail.com','asdf','asdf','asdf','asdf','asdf','asdf','2013-08-02 23:23:19','2013-08-04 22:33:01',0),(35,0,'PO','S','baljeetgaheer@gmail.com','BA','ASDF','ASASDF','ASDFA','SDFAS','DFASDF','2013-08-02 23:44:22','2013-08-10 22:14:37',0);
CREATE TABLE `questionanswers` (
  `seq` int(11) NOT NULL AUTO_INCREMENT,
  `questionseq` int(11) DEFAULT NULL,
  `title` varchar(500) CHARACTER SET utf8 DEFAULT NULL,
  `iscorrect` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=797 DEFAULT CHARSET=latin1;
INSERT INTO `questionanswers` (`seq`,`questionseq`,`title`,`iscorrect`) VALUES (681,150,'aa1',0),(682,150,'aa2',1),(683,150,'aa3',0),(684,150,'aa4',0),(685,151,'ssf',0),(686,151,'sdg',0),(687,151,'sdgg',0),(688,151,'sdf',1),(689,152,'ssf',0),(690,152,'sdg',0),(691,152,'sdgg',0),(692,152,'sdf',1),(693,153,'ans1',1),(694,153,'ans2',0),(695,153,'ans3',0),(696,153,'ans4',0),(697,154,'ans1_1',0),(698,154,'ans2_1',1),(699,154,'ans3_1',0),(700,154,'ans4_1',0),(701,155,'ans1_2',0),(702,155,'ans2_2',0),(703,155,'ans3_2',1),(704,155,'ans4_2',0),(705,156,'ans1',1),(706,156,'ans2',0),(707,156,'ans3',0),(708,156,'ans4',0),(709,157,'ans1_1',0),(710,157,'ans2_1',1),(711,157,'ans3_1',0),(712,157,'ans4_1',0),(713,158,'ans1_2',0),(714,158,'ans2_2',0),(715,158,'ans3_2',1),(716,158,'ans4_2',0),(717,159,'ad1',0),(718,159,'ad2',1),(719,159,'ad3',0),(720,159,'ad4',0),(721,160,'ans1',1),(722,160,'ans2',0),(723,160,'ans3',0),(724,160,'ans4',0),(725,161,'ans1_1',0),(726,161,'ans2_1',1),(727,161,'ans3_1',0),(728,161,'ans4_1',0),(729,162,'ans1_2',0);
INSERT INTO `questionanswers` (`seq`,`questionseq`,`title`,`iscorrect`) VALUES (730,162,'ans2_2',0),(731,162,'ans3_2',1),(732,162,'ans4_2',0),(733,163,'aa1',0),(734,163,'aa2',1),(735,163,'aa3',0),(736,163,'aa4',0),(737,164,'a',0),(738,164,' b',0),(739,164,'c',1),(740,164,'d',0),(741,165,'10.0',0),(742,165,'20.0',0),(743,165,'30.0',0),(744,165,'32.0',1),(745,166,'Munish',1),(746,166,'Harish',0),(747,166,'Suresh',0),(748,166,'Ramesh',0),(749,167,'aa1',0),(750,167,'aa2',0),(751,167,'aa3',1),(752,167,'aa4',0),(753,168,'as',0),(754,168,'s',0),(755,168,'s',0),(756,168,'s',0),(757,169,'asdf',0),(758,169,'df',0),(759,169,'df',1),(760,169,'fd',0),(761,170,'ddd',0),(762,170,'d',0),(763,170,'d',1),(764,170,'dd',0),(765,171,'aa',0),(766,171,'aa',0),(767,171,'aa',0),(768,171,'aa',0),(769,172,'ass',0),(770,172,'sdf',0),(771,172,'fa',1),(772,172,'fs',0),(773,173,'te',0),(774,173,'te',0),(775,173,'te',0),(776,173,'te',0),(777,174,'dd',0),(778,174,'d',0),(779,174,'ddd',0),(780,174,'dd',1),(781,175,'10.0',0),(782,175,'20.0',0);
INSERT INTO `questionanswers` (`seq`,`questionseq`,`title`,`iscorrect`) VALUES (783,175,'30.0',0),(784,175,'32.0',1),(785,176,'Munish',1),(786,176,'Harish',0),(787,176,'Suresh',0),(788,176,'Ramesh',0),(789,177,'dd',0),(790,177,'dd',0),(791,177,'dd',1),(792,177,'dd',0),(793,178,'dd',0),(794,178,'dd',0),(795,178,'dd',1),(796,178,'dd',0);
CREATE TABLE `questions` (
  `seq` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(200) CHARACTER SET utf8 DEFAULT NULL,
  `description` text,
  `points` bigint(20) DEFAULT NULL,
  `projectseq` int(11) DEFAULT NULL,
  `createdon` datetime DEFAULT NULL,
  `lastmodified` datetime DEFAULT NULL,
  `isenabled` tinyint(4) DEFAULT NULL,
  `negativepoints` int(11) DEFAULT NULL,
  `maxsecondsallowed` int(11) DEFAULT NULL,
  `extraattemptsallowed` int(11) DEFAULT NULL,
  `hint` varchar(1000) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=179 DEFAULT CHARSET=latin1;
INSERT INTO `questions` (`seq`,`title`,`description`,`points`,`projectseq`,`createdon`,`lastmodified`,`isenabled`,`negativepoints`,`maxsecondsallowed`,`extraattemptsallowed`,`hint`) VALUES (150,'qes','des',34,31,'2013-10-24 22:36:25','2013-10-24 22:36:25',0,2,34,34,NULL),(151,'test deq','ss',4,31,'2013-10-24 23:06:04','2013-10-24 23:06:04',0,1,44,44,NULL),(152,'test deq','ss',4,31,'2013-10-24 23:06:51','2013-10-24 23:06:51',0,1,44,44,NULL),(153,'Question','questionDes',200,31,NULL,NULL,1,10,30,1,'hh'),(154,'Question1','questionDes1',201,31,NULL,NULL,1,11,31,2,'hh1'),(155,'Question2','questionDes2',202,31,NULL,NULL,1,12,32,3,'hh2'),(156,'Question','questionDes',200,31,NULL,NULL,1,10,30,1,'hh'),(157,'Question1','questionDes1',201,31,NULL,NULL,1,11,31,2,'hh1'),(158,'Question2','questionDes2',202,31,NULL,NULL,1,12,32,3,'hh2'),(159,'des','dd',5,31,'2013-10-24 23:22:02','2013-10-24 23:22:02',0,4,45,45,NULL),(160,'Question','questionDes',200,31,NULL,NULL,1,10,30,1,'hh'),(161,'Question1','questionDes1',201,31,NULL,NULL,1,11,31,2,'hh1'),(162,'Question2','questionDes2',202,31,NULL,NULL,1,12,32,3,'hh2');
INSERT INTO `questions` (`seq`,`title`,`description`,`points`,`projectseq`,`createdon`,`lastmodified`,`isenabled`,`negativepoints`,`maxsecondsallowed`,`extraattemptsallowed`,`hint`) VALUES (163,'Ques12','des',45,31,'2013-10-25 15:39:14','2013-10-25 15:39:14',0,1,0,0,NULL),(164,'ques2','des',34,31,'2013-10-25 15:40:36','2013-10-25 15:40:36',0,0,0,0,NULL),(165,'What is your Age?',NULL,100,31,NULL,NULL,1,10,30,2,NULL),(166,'What is Your Name?',NULL,120,31,NULL,NULL,1,0,0,0,NULL),(167,'test ','des',5,31,'2013-11-21 20:40:13','2013-11-21 20:40:13',0,2,3,3,NULL),(168,'test qes','des',4,31,'2013-11-21 21:29:00','2013-11-21 21:29:00',0,2,4,4,NULL),(169,'test','dd',3,31,'2013-11-21 21:29:11','2013-11-21 21:29:11',0,2,2,2,NULL),(170,'test','dd',5,31,'2013-11-21 21:31:59','2013-11-21 21:31:59',0,2,3,3,NULL),(171,'tt','des',5,31,'2013-11-21 21:37:51','2013-11-21 21:37:51',0,2,3,3,NULL),(172,'ssrwr','rd',3,31,'2013-11-21 21:40:59','2013-11-21 21:40:59',0,2,3,3,NULL),(173,'tett','tet',4,31,'2013-11-21 21:41:34','2013-11-21 21:41:34',0,2,3,3,NULL),(174,'test 1234','des',0,31,'2013-11-21 21:44:08','2013-11-21 21:44:08',0,0,4,4,NULL);
INSERT INTO `questions` (`seq`,`title`,`description`,`points`,`projectseq`,`createdon`,`lastmodified`,`isenabled`,`negativepoints`,`maxsecondsallowed`,`extraattemptsallowed`,`hint`) VALUES (175,'What is your Age?',NULL,100,31,NULL,NULL,1,10,30,2,NULL),(176,'What is Your Name?',NULL,120,31,NULL,NULL,1,0,0,0,NULL),(177,'test 123','des',0,31,'2013-11-23 15:27:14','2013-11-23 15:27:14',0,0,22,22,NULL),(178,'test234','des',3,31,'2013-11-23 15:29:10','2013-11-23 15:29:10',0,1,2,2,NULL);
CREATE TABLE `scores` (
  `seq` int(11) NOT NULL AUTO_INCREMENT,
  `userseq` int(11) DEFAULT NULL,
  `gameseq` int(11) DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  `timetaken` int(11) DEFAULT NULL,
  `createdon` datetime DEFAULT NULL,
  PRIMARY KEY (`seq`),
  UNIQUE KEY `uniue user game` (`gameseq`,`userseq`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
INSERT INTO `scores` (`seq`,`userseq`,`gameseq`,`score`,`timetaken`,`createdon`) VALUES (1,3,1,50,120,'2013-06-20 00:00:00'),(2,1,1,100,70,'2013-06-20 18:21:43'),(3,2,1,75,81,'2013-06-19 18:22:19'),(5,1,4,50,0,NULL),(6,34,2,40,0,NULL);
CREATE TABLE `setgames` (
  `seq` int(11) NOT NULL AUTO_INCREMENT,
  `setseq` int(11) DEFAULT NULL,
  `gameseq` int(11) DEFAULT NULL,
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
CREATE TABLE `sets` (
  `seq` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(250) DEFAULT NULL,
  `description` varchar(250) DEFAULT NULL,
  `createdon` datetime DEFAULT NULL,
  `projectseq` int(11) DEFAULT NULL,
  `lastmodifieddate` datetime DEFAULT NULL,
  `isenabled` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
INSERT INTO `sets` (`seq`,`name`,`description`,`createdon`,`projectseq`,`lastmodifieddate`,`isenabled`) VALUES (1,'set3','ded','2013-07-07 12:22:50',1,'2013-07-11 20:45:15',1),(2,'set2','desc','2013-07-08 22:12:10',1,NULL,NULL),(3,'set2','descddd','2013-07-08 22:50:55',1,NULL,NULL),(4,'dfd','fdfdf','2013-07-11 20:47:37',1,'2013-07-11 20:47:37',1),(5,'teste','dd','2013-08-06 09:04:36',31,'2013-09-25 22:41:13',1);
CREATE TABLE `usergroups` (
  `seq` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(250) DEFAULT NULL,
  `description` varchar(250) DEFAULT NULL,
  `createdon` datetime DEFAULT NULL,
  `projectseq` int(11) DEFAULT NULL,
  `lastmodifieddate` datetime DEFAULT NULL,
  `isenabled` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=latin1;
INSERT INTO `usergroups` (`seq`,`name`,`description`,`createdon`,`projectseq`,`lastmodifieddate`,`isenabled`) VALUES (3,'swwas','eess','2013-07-07 20:43:32',31,'2013-09-25 22:40:43',1),(4,'project1','dd','2013-07-08 21:32:49',31,'2013-08-04 22:30:09',0),(5,'project12','dd','2013-07-08 21:34:47',1,'2013-07-11 20:43:20',0),(6,'testg','dd','2013-10-12 22:23:51',31,'2013-10-12 22:23:51',1),(7,'testg','sddfg','2013-10-14 23:04:12',31,'2013-10-14 23:04:12',0),(8,'test gg','des','2013-10-17 21:44:16',31,'2013-10-17 21:44:24',1),(9,'dummyGroup','dummyGroup','2013-10-24 20:36:20',31,'2013-10-24 20:41:28',0),(10,'dummyGroup','dummyGroup','2013-10-24 20:45:57',31,'2013-10-24 20:49:56',0),(11,'dummyGroup','dummyGroup','2013-10-24 21:00:16',31,'2013-10-24 21:01:17',0),(12,'dummyGroup','dummyGroup','2013-10-24 21:07:17',31,'2013-10-24 21:07:17',0),(13,'dummyGroup','dummyGroup','2013-10-24 21:08:56',31,'2013-10-24 21:08:56',0),(14,'dummyGroup','dummyGroup','2013-10-25 15:49:22',31,'2013-10-25 15:50:28',0),(15,'dummyGroup','dummyGroup','2013-10-31 23:32:54',31,'2013-10-31 23:33:00',0);
INSERT INTO `usergroups` (`seq`,`name`,`description`,`createdon`,`projectseq`,`lastmodifieddate`,`isenabled`) VALUES (16,'dummyGroup','dummyGroup','2013-11-01 22:29:22',31,'2013-11-01 22:29:28',0),(17,'dummyGroup','dummyGroup','2013-11-06 20:59:40',31,'2013-11-06 20:59:45',0),(18,'dummyGroup','dummyGroup','2013-11-06 21:06:46',31,'2013-11-06 21:06:53',0),(19,'dummyGroup1','dummyGroup2','2013-11-06 21:13:57',31,'2013-11-06 22:02:54',0),(20,'New User Group','des','2013-11-20 20:32:11',31,'2013-11-20 20:32:18',0),(21,'','','2013-11-20 20:32:48',31,'2013-11-20 20:32:49',0),(22,'new user','des','2013-11-20 20:47:43',31,'2013-11-20 20:47:43',0),(23,'','','2013-11-20 20:48:28',31,'2013-11-20 20:48:29',0),(24,'','','2013-11-20 20:48:44',31,'2013-11-20 20:48:44',0),(25,'','','2013-11-20 21:28:48',31,'2013-11-20 21:28:48',0),(26,'test desgg','dd','2013-11-20 21:29:58',31,'2013-11-20 21:30:34',0),(27,'test',' des','2013-11-23 15:37:20',31,'2013-11-23 15:37:20',0);
CREATE TABLE `usergroupusers` (
  `seq` int(11) NOT NULL AUTO_INCREMENT,
  `userseq` int(11) DEFAULT NULL,
  `usergroupseq` int(11) DEFAULT NULL,
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=latin1;
INSERT INTO `usergroupusers` (`seq`,`userseq`,`usergroupseq`) VALUES (2,2,3),(3,3,3),(4,4,15),(5,5,16),(6,6,17),(7,7,18),(10,10,19),(11,11,20),(12,12,21),(13,13,22),(14,14,23),(15,15,24),(16,16,25),(18,18,26),(19,19,27);
CREATE TABLE `users` (
  `seq` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(150) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `location` varchar(150) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `createdon` datetime DEFAULT NULL,
  `projectseq` int(11) DEFAULT NULL,
  `department` varchar(200) DEFAULT NULL,
  `isenabled` tinyint(4) DEFAULT NULL,
  `lastmodifieddate` datetime DEFAULT NULL,
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=latin1 DELAY_KEY_WRITE=1;
INSERT INTO `users` (`seq`,`name`,`email`,`mobile`,`location`,`username`,`password`,`createdon`,`projectseq`,`department`,`isenabled`,`lastmodifieddate`) VALUES (1,'bg','baljeetSingh','12345','lbl','ngaheer','lincoln9','2013-10-25 15:49:22',31,NULL,0,'2013-10-25 15:49:22'),(2,'Baljeetsg','bs@gmail.com','9.41756869E8','test location','bgaheer','ss','2013-10-25 15:50:22',31,'test dept',1,'2013-10-25 15:50:22'),(3,'Baljeetgh','bs1@gmail.com','9.417568691E9','test location1','bgaheer1','tt','2013-10-25 15:50:22',31,'test dept1',1,'2013-10-25 15:50:22'),(4,'baljeet Singh','baljeetgaheer@gmail.com','1234567','lo','uu','pp','2013-10-31 23:32:58',31,NULL,0,'2013-10-31 23:32:58'),(5,'test','baljeetgaheer@gmail.com','1234','ll','uu','pp','2013-11-01 22:29:24',31,NULL,0,'2013-11-01 22:29:24'),(6,'test 123','baljeetgaheer@gmail.com','12345','gg','bb','uu','2013-11-06 20:59:43',31,NULL,0,'2013-11-06 20:59:43'),(7,'test334','baljeetgaheer@gmail.com','12345','gg','bb','uu','2013-11-06 21:06:51',31,NULL,0,'2013-11-06 21:06:51'),(8,'mm','baljeetgaheer@gmail.com','12345','locati','test','tt','2013-11-06 21:13:57',31,NULL,0,'2013-11-06 21:13:57');
INSERT INTO `users` (`seq`,`name`,`email`,`mobile`,`location`,`username`,`password`,`createdon`,`projectseq`,`department`,`isenabled`,`lastmodifieddate`) VALUES (9,'test 122','baljeetgaheer@gmail.com','12345','locati','test','tt','2013-11-06 21:50:42',31,NULL,0,'2013-11-06 21:50:42'),(10,'tt','baljeetgaheer@gmail.com','1222','locati','test','tt','2013-11-06 22:02:42',31,NULL,0,'2013-11-06 22:02:42'),(11,'test','baljeetgaheer@gmail.com','mm','dd','dd','dd','2013-11-20 20:32:16',31,NULL,0,'2013-11-20 20:32:16'),(12,'','','','','','','2013-11-20 20:32:48',31,NULL,0,'2013-11-20 20:32:48'),(13,'dd','baljeetgaheer@gmail.com','dd','dd','dd','dd','2013-11-20 20:47:43',31,NULL,0,'2013-11-20 20:47:43'),(14,'','','','','','','2013-11-20 20:48:28',31,NULL,0,'2013-11-20 20:48:28'),(15,'','','','','','','2013-11-20 20:48:44',31,NULL,0,'2013-11-20 20:48:44'),(16,'','','','','','','2013-11-20 21:28:48',31,NULL,0,'2013-11-20 21:28:48'),(17,'test','tt','tt','tt','tt','tt','2013-11-20 21:29:58',31,NULL,0,'2013-11-20 21:29:58'),(18,'','tt','tt','tt','tt','tt','2013-11-20 21:30:34',31,NULL,0,'2013-11-20 21:30:34');
INSERT INTO `users` (`seq`,`name`,`email`,`mobile`,`location`,`username`,`password`,`createdon`,`projectseq`,`department`,`isenabled`,`lastmodifieddate`) VALUES (19,'dd','d','dd','dd','dd','dd','2013-11-23 15:37:20',31,NULL,0,'2013-11-23 15:37:20');
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT;
SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS;
SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
