CREATE SCHEMA IF NOT EXISTS `sql9317046` ;

CREATE TABLE `review` (
  `review_id` INT NOT NULL AUTO_INCREMENT,
  `review_rating` INT NOT NULL DEFAULT 0,
  `name` VARCHAR(45) NOT NULL,
  `review_body` TEXT NULL,
  `author` VARCHAR(45) NOT NULL,
  `publisher` VARCHAR(100) NULL DEFAULT 'Udacity',
  PRIMARY KEY (`review_id`));

CREATE TABLE  `product` (
  `product_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `description` VARCHAR(250) NOT NULL,
  `image` VARCHAR(250) NULL,
  PRIMARY KEY (`product_id`));

ALTER TABLE `review`
ADD COLUMN `product_id` INT NOT NULL AFTER `publisher`,
ADD INDEX `product_id_idx` (`product_id` ASC);
ALTER TABLE `review`
ADD CONSTRAINT `product_id`
  FOREIGN KEY (`product_id`)
  REFERENCES `product` (`product_id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

CREATE TABLE  `comment` (
  `comment_id` INT NOT NULL AUTO_INCREMENT,
  `comment_body` TEXT NOT NULL,
  `upvote_count` INT NULL DEFAULT 0,
  `downvote_count` INT NULL DEFAULT 0,
  `review_id` INT NOT NULL,
  PRIMARY KEY (`comment_id`),
  INDEX `review_id_idx` (`review_id` ASC),
  CONSTRAINT `review_id`
    FOREIGN KEY (`review_id`)
    REFERENCES `review` (`review_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);