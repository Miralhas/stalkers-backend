ALTER TABLE novel_chapters
    DROP FOREIGN KEY fk_novcha_on_chapter;

ALTER TABLE novel_chapters
    DROP FOREIGN KEY fk_novcha_on_novel;

ALTER TABLE chapter
    ADD novel_id BIGINT NULL;

ALTER TABLE chapter
    ADD CONSTRAINT FK_CHAPTER_ON_NOVEL FOREIGN KEY (novel_id) REFERENCES novel (id);

DROP TABLE novel_chapters;