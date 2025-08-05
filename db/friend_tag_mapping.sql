CREATE TABLE `friend_tag_mapping` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `friend_user_id` BIGINT UNSIGNED NOT NULL COMMENT '好友ID',
  `tag_id` INT UNSIGNED NOT NULL COMMENT '标签ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_mapping` (`user_id`, `friend_user_id`, `tag_id`) -- 避免重复标记
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='好友标签关联表';