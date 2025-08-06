CREATE TABLE elders_choice
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    novel_id BIGINT                NULL,
    CONSTRAINT pk_elderschoice PRIMARY KEY (id)
);

ALTER TABLE elders_choice
    ADD CONSTRAINT FK_ELDERSCHOICE_ON_NOVELS FOREIGN KEY (novel_id) REFERENCES novel (id);