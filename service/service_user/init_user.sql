SET NAMES utf8 ;


DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(8) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL UNIQUE COMMENT '昵称',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `addtime` datetime(0) NOT NULL COMMENT '注册时间',
  `comment` tinyint(1) NOT NULL DEFAULT 0 COMMENT '为0可以发言，1被禁言',
  `type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0-普通用户， 1-超管，2-版主',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0-未激活; 1-已激活;',
  `sex` tinyint(1) NOT NULL DEFAULT 0 COMMENT '性别：0保密；1男；2女',
  `qq` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  COMMENT 'qq号',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL UNIQUE COMMENT '邮箱',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '电话',
  `activation_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '激活码',

  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'avatar.jpg' COMMENT '头像',
--   `posts_num` int(8) NOT NULL DEFAULT 0 COMMENT '发微博的数量',
--   `follows_num` int(8) NOT NULL DEFAULT 0 COMMENT '关注数量',
--   `fans_num` int(8) NOT NULL DEFAULT 0 COMMENT '粉丝数量',

  `college` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '学校',
  `gpa` double(4, 2) NOT NULL DEFAULT 0.00 COMMENT 'gpa',
  `sat` int(8) NOT NULL DEFAULT 0 COMMENT 'sat',
  `ielts` double(6, 2) NOT NULL DEFAULT 0.00 COMMENT '雅思',
  `toefl` int(8) NOT NULL DEFAULT 0 COMMENT '托福',
--   `recommend` json NOT NULL COMMENT '意向院校，里面是一个json数组，长度为150，下标为学校的qs排名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- 微博-用户相关



insert  into `user`(`id`,`username`,`password`,`addtime`,`comment`,`type`,`status`,`sex`,`qq`,`email`,`phone`,`activation_code`,`avatar`,`college`,`gpa`,`sat`,`ielts`,`toefl`) values (18,'crea','202cb962ac59075b964b07152d234b70','2021-06-08 09:24:10',0,1,1,1,'45557891','Lanjun1998@163.com','1839999123123','ff65951e9fb9422bafe009a185509645','http://creasbucket.oss-cn-shanghai.aliyuncs.com/project4_header/th.jpg',NULL,3.99,1500,6.50,110);

