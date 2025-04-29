CREATE TABLE genre
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(255)          NOT NULL,
    `description` TEXT                  NULL,
    CONSTRAINT pk_genre PRIMARY KEY (id)
);

CREATE TABLE novel_genres
(
    genre_id BIGINT NOT NULL,
    novel_id BIGINT NOT NULL,
    CONSTRAINT pk_novel_genres PRIMARY KEY (genre_id, novel_id)
);

ALTER TABLE genre
    ADD CONSTRAINT uc_genre_name UNIQUE (name);

ALTER TABLE novel_genres
    ADD CONSTRAINT fk_novgen_on_genre FOREIGN KEY (genre_id) REFERENCES genre (id);

ALTER TABLE novel_genres
    ADD CONSTRAINT fk_novgen_on_novel FOREIGN KEY (novel_id) REFERENCES novel (id);