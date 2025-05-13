ALTER TABLE tag ADD CONSTRAINT uc_tag_slug UNIQUE (slug);
ALTER TABLE genre ADD CONSTRAINT uc_genre_slug UNIQUE (slug);