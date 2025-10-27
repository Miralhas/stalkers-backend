CREATE TABLE chapter_error
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    slug          VARCHAR(255)          NOT NULL,
    name          VARCHAR(255)          NOT NULL,
    `description` TEXT                  NOT NULL,
    CONSTRAINT pk_chaptererror PRIMARY KEY (id)
);

CREATE TABLE fix_chapter_request_errors
(
    error_id   BIGINT NOT NULL,
    request_id BIGINT NOT NULL,
    CONSTRAINT pk_fix_chapter_request_errors PRIMARY KEY (error_id, request_id)
);

ALTER TABLE request ADD chapter_id BIGINT NULL;
ALTER TABLE request ADD  another_reason TEXT NULL;

ALTER TABLE chapter_error
    ADD CONSTRAINT uc_chaptererror_slug UNIQUE (slug);

ALTER TABLE request
    ADD CONSTRAINT FK_REQUEST_ON_CHAPTER FOREIGN KEY (chapter_id) REFERENCES chapter (id);

ALTER TABLE fix_chapter_request_errors
    ADD CONSTRAINT fk_fixchareqerr_on_chapter_errors FOREIGN KEY (error_id) REFERENCES chapter_error (id);

ALTER TABLE fix_chapter_request_errors
    ADD CONSTRAINT fk_fixchareqerr_on_fix_chapter_request FOREIGN KEY (request_id) REFERENCES request (id);