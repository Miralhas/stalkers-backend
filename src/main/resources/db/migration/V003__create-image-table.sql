CREATE TABLE image
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    file_name     VARCHAR(255) NOT NULL,
    content_type  VARCHAR(255) NOT NULL,
    size          BIGINT       NOT NULL,
    `description` VARCHAR(255) NULL,
    CONSTRAINT pk_image PRIMARY KEY (id)
);