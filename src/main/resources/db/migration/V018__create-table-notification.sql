CREATE TABLE notification (
    id                       BIGINT AUTO_INCREMENT NOT NULL,
    notifcation_type         VARCHAR(31)           NULL,
    is_read                  BIT(1)                NOT NULL,
    created_at               datetime              NULL,
    title                    VARCHAR(255)          NOT NULL,
    `description`            TEXT                  NOT NULL,
    new_chapter_slug         VARCHAR(255)          NULL,
    novel_slug               VARCHAR(255)          NULL,
    new_chapter_release_date datetime              NULL,
    CONSTRAINT pk_notification PRIMARY KEY (id)
);

CREATE TABLE notification_recipients (
    notification_id BIGINT NOT NULL,
    recipient_id    BIGINT NOT NULL,
    CONSTRAINT pk_notification_recipients PRIMARY KEY (notification_id, recipient_id)
);

ALTER TABLE notification_recipients
    ADD CONSTRAINT fk_notrec_on_notification FOREIGN KEY (notification_id) REFERENCES notification (id);

ALTER TABLE notification_recipients
    ADD CONSTRAINT fk_notrec_on_user FOREIGN KEY (recipient_id) REFERENCES user (id);