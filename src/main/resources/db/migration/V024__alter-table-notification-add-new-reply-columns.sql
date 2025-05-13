ALTER TABLE notification ADD COLUMN user_replying VARCHAR(255);
ALTER TABLE notification ADD COLUMN reply_comment_content TEXT;
ALTER TABLE notification ADD COLUMN parent_comment_content TEXT;
ALTER TABLE notification ADD COLUMN uri VARCHAR(255);