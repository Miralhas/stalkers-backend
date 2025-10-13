# Most of these columns are used when querying for novels (JPA Specification)

CREATE INDEX idx_novel_title ON novel(title);
CREATE INDEX idx_novel_alias ON novel(alias(255));

CREATE INDEX idx_novel_author ON novel(author);
CREATE INDEX idx_novel_status ON novel(status);
CREATE INDEX idx_novel_is_hidden ON novel(is_hidden);

CREATE INDEX idx_novel_hidden_status ON novel(is_hidden, status);

# Join Tables

CREATE INDEX idx_novel_tags_novel_id ON novel_tags(novel_id);
CREATE INDEX idx_novel_tags_tag_id ON novel_tags(tag_id);

CREATE INDEX idx_novel_genres_novel_id ON novel_genres(novel_id);
CREATE INDEX idx_novel_genres_genre_id ON novel_genres(genre_id);