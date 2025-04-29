CREATE TABLE chapter
(
    id    BIGINT AUTO_INCREMENT NOT NULL,
    title VARCHAR(255) NOT NULL,
    body  TEXT         NOT NULL,
    slug  VARCHAR(255) NOT NULL,
    CONSTRAINT pk_chapter PRIMARY KEY (id)
);

CREATE TABLE novel
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    slug          VARCHAR(255) NOT NULL,
    title         VARCHAR(255) NOT NULL,
    author        VARCHAR(255) NOT NULL,
    status        VARCHAR(255) NOT NULL,
    `description` VARCHAR(255) NULL,
    image_id      BIGINT NULL,
    CONSTRAINT pk_novel PRIMARY KEY (id)
);

CREATE TABLE tag
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(255)          NOT NULL,
    `description` TEXT                  NULL,
    CONSTRAINT pk_tag PRIMARY KEY (id)
);

ALTER TABLE tag
    ADD CONSTRAINT uc_tag_name UNIQUE (name);

CREATE TABLE novel_chapters
(
    novel_id    BIGINT NOT NULL,
    chapters_id BIGINT NOT NULL
);

CREATE TABLE novel_tags
(
    novel_id BIGINT NOT NULL,
    tag_id   BIGINT NOT NULL,
    CONSTRAINT pk_novel_tags PRIMARY KEY (novel_id, tag_id)
);

ALTER TABLE chapter
    ADD CONSTRAINT uc_chapter_slug UNIQUE (slug);

ALTER TABLE novel_chapters
    ADD CONSTRAINT uc_novel_chapters_chapters UNIQUE (chapters_id);

ALTER TABLE novel
    ADD CONSTRAINT uc_novel_slug UNIQUE (slug);

ALTER TABLE novel
    ADD CONSTRAINT FK_NOVEL_ON_IMAGE FOREIGN KEY (image_id) REFERENCES image (id);

ALTER TABLE novel_chapters
    ADD CONSTRAINT fk_novcha_on_chapter FOREIGN KEY (chapters_id) REFERENCES chapter (id);

ALTER TABLE novel_chapters
    ADD CONSTRAINT fk_novcha_on_novel FOREIGN KEY (novel_id) REFERENCES novel (id);

ALTER TABLE novel_tags
    ADD CONSTRAINT fk_novtag_on_novel FOREIGN KEY (novel_id) REFERENCES novel (id);

ALTER TABLE novel_tags
    ADD CONSTRAINT fk_novtag_on_tags FOREIGN KEY (tag_id) REFERENCES tag (id);