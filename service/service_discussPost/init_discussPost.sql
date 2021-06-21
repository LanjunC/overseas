SET NAMES utf8 ;


-- 论坛相关


DROP TABLE IF EXISTS `discuss_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `discuss_post` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(8)  NOT NULL COMMENT '以后再加外键;',
  `college_id` int(8) NOT NULL DEFAULT 0 COMMENT '以后再加外键,0表示跟学校无关的帖子;',
--   `major_id` int(8) UNSIGNED NOT NULL,COMMENT '以后再加外键;',
  `title` varchar(100) DEFAULT NULL,
  `content` text,
  `type` int(11) NOT NULL DEFAULT 0 COMMENT '0-普通; 1-置顶;',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT '0-正常; 1-精华; 2-拉黑;',
  `create_time` timestamp NULL DEFAULT NULL,
  `comment_count` int(11) NOT NULL DEFAULT 0,
  `score` double NOT NULL DEFAULT 0.0,
  PRIMARY KEY (`id`),
  KEY `index_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `entity_type` int(11) DEFAULT NULL,
  `entity_id` int(11) DEFAULT NULL,
  `target_id` int(11) DEFAULT NULL,
  `content` text,
  `status` int(11) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_user_id` (`user_id`) /*!80000 INVISIBLE */,
  KEY `index_entity_id` (`entity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


insert  into `discuss_post`(`id`,`user_id`,`college_id`,`title`,`content`,`type`,`status`,`create_time`,`comment_count`,`score`) values (1,18,0,'测试帖子hello','第一条帖子',0,0,'2021-06-08 18:09:35',0,0),(2,18,0,'测试帖子hello','第一条帖子',0,0,'2021-06-09 07:54:12',0,0),(3,18,0,'测试帖子hello','第一条帖子',0,0,'2021-06-09 08:30:17',0,0),(4,18,0,'测试帖子hello','第一条帖子',0,0,'2021-06-09 08:30:18',0,0),(5,18,0,'测试帖子hello','第一条帖子',0,0,'2021-06-09 08:30:18',0,0),(6,18,0,'测试帖子hello','第一条帖子',0,0,'2021-06-09 08:30:18',0,0),(7,18,0,'测试帖子hello','第一条帖子',0,0,'2021-06-09 08:30:18',0,0),(8,18,0,'测试帖子hello','第一条帖子',0,0,'2021-06-09 08:30:18',0,0),(9,18,0,'测试帖子hello','第一条帖子',0,0,'2021-06-09 08:30:18',0,0),(10,18,0,'测试帖子hello','第一条帖子',0,0,'2021-06-09 08:30:18',0,0),(11,18,0,'测试帖子hello','第一条帖子',0,0,'2021-06-09 08:30:18',0,0),(12,18,0,'测试帖子hello','第一条帖子',0,0,'2021-06-09 08:30:18',0,0),(13,18,0,'测试帖子hello','第一条帖子',0,0,'2021-06-09 08:30:18',0,0),(14,18,0,'测试帖子hello','第一条帖子',0,0,'2021-06-09 08:30:18',0,0),(15,18,0,'测试帖子hello','第一条帖子',0,0,'2021-06-09 08:30:18',0,0),(16,18,0,'测试帖子hello','第一条帖子',0,0,'2021-06-09 08:30:18',0,0),(17,18,0,'测试帖子hello','第一条帖子',0,0,'2021-06-09 08:30:18',0,0),(18,18,0,'测试帖子hello','第一条帖子',0,0,'2021-06-09 08:30:18',0,0),(19,18,0,'测试帖子hello','第一条帖子',0,0,'2021-06-09 08:30:18',0,0),(20,19,0,'测试帖子hello','第一条帖子',1,0,'2021-06-09 08:30:18',0,0),(21,19,0,'测试帖子hello','第一条帖子',0,0,'2021-06-09 08:30:18',0,0),(23,18,0,'测试发帖','这是一条测试发帖啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊阿瓦我',0,0,'2021-06-09 09:25:07',0,0),(25,19,0,'测试帖子hello','第一条帖子',0,0,'2021-06-09 08:30:18',0,0);
insert  into `comment`(`id`,`user_id`,`entity_type`,`entity_id`,`target_id`,`content`,`status`,`create_time`) values (2,18,0,5,0,'这是一条针对帖子的评论：entityType为0表示这是对帖子的评论；这个帖子的id为5；targetId无用，设为0。',0,'2021-06-17 11:44:49'),(3,18,1,2,0,'这是一条针对一级评论的回复（即二级评论）：entityType为1表示这是回复；这个被回复的评论的id为2；targetId无用，设为0。',0,'2021-06-17 11:47:04'),(4,18,1,2,19,'这是一条针对一级评论的回复（即二级评论）：entityType为1表示这是回复；这个被回复的评论的id为2；targetId为19，表示被回复的人的用户id为19，这个数据可用于显示“某某回复某某”。',0,'2021-06-17 11:53:54');

