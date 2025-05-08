CREATE TABLE `comments`
(
    `is_spoiler`        BIT(1)      NOT NULL,
    `chapter_id`        BIGINT      NULL DEFAULT NULL,
    `commenter_id`      BIGINT      NOT NULL,
    `created_at`        DATETIME(6) NULL DEFAULT NULL,
    `id`                BIGINT      NOT NULL AUTO_INCREMENT,
    `novel_id`          BIGINT      NULL DEFAULT NULL,
    `parent_comment_id` BIGINT      NULL DEFAULT NULL,
    `updated_at`        DATETIME(6) NULL DEFAULT NULL,
    `comment_type`      VARCHAR(31) NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
    `message`           TEXT        NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `FK8ctwspillslc6q169ldntqnt4` (`commenter_id`) USING BTREE,
    INDEX `FK7h839m3lkvhbyv3bcdv7sm4fj` (`parent_comment_id`) USING BTREE,
    INDEX `FKc4nbwu4i5ybuk6dqfjoyuceu1` (`chapter_id`) USING BTREE,
    INDEX `FKlsg57l7wpjy0y86dhdb7mnw1s` (`novel_id`) USING BTREE,
    CONSTRAINT `FK7h839m3lkvhbyv3bcdv7sm4fj` FOREIGN KEY (`parent_comment_id`) REFERENCES `comments` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT `FK8ctwspillslc6q169ldntqnt4` FOREIGN KEY (`commenter_id`) REFERENCES `user` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT `FKc4nbwu4i5ybuk6dqfjoyuceu1` FOREIGN KEY (`chapter_id`) REFERENCES `chapter` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT `FKlsg57l7wpjy0y86dhdb7mnw1s` FOREIGN KEY (`novel_id`) REFERENCES `novel` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION
) COLLATE = 'utf8mb4_0900_ai_ci'
  ENGINE = InnoDB;

CREATE TABLE `vote`
(
    `count`      INT                        NOT NULL,
    `comment_id` BIGINT                     NULL DEFAULT NULL,
    `id`         BIGINT                     NOT NULL AUTO_INCREMENT,
    `user_id`    BIGINT                     NULL DEFAULT NULL,
    `type`       ENUM ('DOWNVOTE','UPVOTE') NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `FKlrexare4b5736ith0jmtn6i7n` (`comment_id`) USING BTREE,
    INDEX `FKcsaksoe2iepaj8birrmithwve` (`user_id`) USING BTREE,
    CONSTRAINT `FKcsaksoe2iepaj8birrmithwve` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT `FKlrexare4b5736ith0jmtn6i7n` FOREIGN KEY (`comment_id`) REFERENCES `comments` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION
)
    COLLATE = 'utf8mb4_0900_ai_ci'
    ENGINE = InnoDB;