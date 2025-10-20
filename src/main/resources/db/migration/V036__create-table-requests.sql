CREATE TABLE request
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    request_type VARCHAR(31)           NULL,
    created_at   datetime              NULL,
    user_id      BIGINT                NOT NULL,
    novel_id     BIGINT                NULL,
    novel_title  VARCHAR(255)          NOT NULL,
    `status` ENUM('PENDING','COMPLETED','CANCELED') NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
    CONSTRAINT pk_request PRIMARY KEY (id),
    INDEX `FK_REQUEST_TYPE` (`request_type`) USING BTREE
);

ALTER TABLE request
    ADD CONSTRAINT FK_REQUEST_ON_NOVEL FOREIGN KEY (novel_id) REFERENCES novel (id);

ALTER TABLE request
    ADD CONSTRAINT FK_REQUEST_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);