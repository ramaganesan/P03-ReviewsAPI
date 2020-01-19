ALTER TABLE `comment`
ADD COLUMN `create_dt` DATETIME NULL AFTER `review_id`,
ADD COLUMN `update_dt` DATETIME NULL AFTER `create_dt`;

ALTER TABLE `product`
ADD COLUMN `create_dt` DATETIME NULL AFTER `image`,
ADD COLUMN `update_dt` DATETIME NULL AFTER `create_dt`;

ALTER TABLE `review`
ADD COLUMN `create_dt` DATETIME NULL AFTER `publisher`,
ADD COLUMN `update_dt` DATETIME NULL AFTER `create_dt`;