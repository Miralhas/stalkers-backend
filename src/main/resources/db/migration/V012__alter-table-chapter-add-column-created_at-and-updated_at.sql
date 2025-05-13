ALTER TABLE chapter ADD created_at DATETIME(6) NULL DEFAULT NULL;
ALTER TABLE chapter ADD updated_at DATETIME(6) NULL DEFAULT NULL;

UPDATE chapter SET created_at = UTC_TIMESTAMP where chapter.created_at IS NULL;
UPDATE chapter SET updated_at = UTC_TIMESTAMP where chapter.updated_at IS NULL;