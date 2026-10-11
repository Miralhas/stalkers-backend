CREATE TABLE series (
    id         BIGINT AUTO_INCREMENT NOT NULL,
    slug       VARCHAR(255)          NOT NULL,
    title      VARCHAR(255)          NOT NULL,
    created_at datetime(6)           NULL,
    updated_at datetime(6)           NULL,
    CONSTRAINT pk_series PRIMARY KEY (id)
);

ALTER TABLE novel
    ADD series_id BIGINT NULL;

ALTER TABLE series
    ADD CONSTRAINT uc_series_slug UNIQUE (slug);

ALTER TABLE novel
    ADD CONSTRAINT FK_NOVEL_ON_SERIES FOREIGN KEY (series_id) REFERENCES series (id);
