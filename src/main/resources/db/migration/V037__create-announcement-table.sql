CREATE TABLE announcement
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    slug       VARCHAR(255)          NOT NULL,
    title      VARCHAR(255)          NOT NULL,
    body       TEXT                  NOT NULL,
    user_id    BIGINT                NOT NULL,
    created_at datetime              NULL,
    updated_at datetime              NULL,
    pinned     BIT(1)                NOT NULL,
    CONSTRAINT pk_announcement PRIMARY KEY (id),
    INDEX `IDX_PINNED` (`pinned`) USING BTREE
);

ALTER TABLE announcement
    ADD CONSTRAINT uc_announcement_slug UNIQUE (slug);

ALTER TABLE announcement
    ADD CONSTRAINT FK_ANNOUNCEMENT_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);