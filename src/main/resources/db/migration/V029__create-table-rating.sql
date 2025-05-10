CREATE TABLE rating (
    id           BIGINT AUTO_INCREMENT NOT NULL,
    novel_id     BIGINT                NULL,
    rating_value DOUBLE                NOT NULL,
    user_id      BIGINT                NULL,
    CONSTRAINT pk_rating PRIMARY KEY (id)
)COLLATE = 'utf8mb4_0900_ai_ci' ENGINE = InnoDB;

ALTER TABLE rating
    ADD CONSTRAINT FK_RATING_ON_NOVEL FOREIGN KEY (novel_id) REFERENCES novel (id);

ALTER TABLE rating
    ADD CONSTRAINT FK_RATING_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);