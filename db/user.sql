CREATE TABLE `im`.`user` (
  `id` BIGINT NOT NULL,
  `username` VARCHAR(64) NOT NULL COMMENT '昵称',
  `password` VARCHAR(64) NOT NULL,
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `deleted` INT NULL COMMENT '状态，1：已经注销，0：正常',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_bin;