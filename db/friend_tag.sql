CREATE TABLE `friend_tag` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '标签ID',
  `user_id` bigint unsigned NOT NULL COMMENT '创建者用户ID',
  `tag_name` varchar(50) NOT NULL COMMENT '标签名称',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='好友标签表';
