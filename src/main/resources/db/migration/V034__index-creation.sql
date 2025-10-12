CREATE INDEX idx_rating ON rating(rating_value);
CREATE INDEX idx_rating_novel_id_value ON rating(novel_id, rating_value);
CREATE INDEX idx_chapter_number ON chapter(number);
