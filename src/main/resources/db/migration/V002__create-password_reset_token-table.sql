CREATE TABLE password_reset_token (
    user_id BIGINT       NOT NULL,
    token   VARCHAR(255) NOT NULL,
    CONSTRAINT pk_passwordresettoken PRIMARY KEY (user_id)
)engine=InnoDB;

ALTER TABLE password_reset_token ADD CONSTRAINT FK_PASSWORDRESETTOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);