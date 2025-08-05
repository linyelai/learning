CREATE TABLE `im`.`user_friend` (
  `id` BIGINT NOT NULL,
  `user_id` BIGINT NULL COMMENT '用户id',
  `friend_id` BIGINT NULL COMMENT '好友id',
  `friend_name` VARCHAR(64) NULL,
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `status` INT NULL COMMENT '状态，0：待同意，1：已经同意，2：删除好友关系',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_bin;
