CREATE TABLE `user_library` (
    `is_bookmarked`      BIT(1)      NOT NULL,
    `current_chapter_id` BIGINT      NOT NULL,
    `id`                 BIGINT      NOT NULL AUTO_INCREMENT,
    `last_read_at`       DATETIME(6) NOT NULL,
    `novel_id`           BIGINT      NOT NULL,
    `user_id`            BIGINT      NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `UKpbs9f2b4cuds69cngf1epm59v` (`user_id`, `novel_id`) USING BTREE,
    INDEX `FKw218ksj4qk7vsxpttm9t6xpm` (`current_chapter_id`) USING BTREE,
    INDEX `FK6fkjaov6xdqe7x0wci9q3bcd` (`novel_id`) USING BTREE,
    CONSTRAINT `FK6fkjaov6xdqe7x0wci9q3bcd` FOREIGN KEY (`novel_id`) REFERENCES `novel` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT `FKc0yldqsbglvvenxcqjuh39tvc` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT `FKw218ksj4qk7vsxpttm9t6xpm` FOREIGN KEY (`current_chapter_id`) REFERENCES `chapter` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION
) COLLATE = 'utf8mb4_0900_ai_ci' ENGINE = InnoDB;