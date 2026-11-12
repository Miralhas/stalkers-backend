CREATE TABLE download_job (
    id     BIGINT AUTO_INCREMENT NOT NULL,
    slug   VARCHAR(255) NOT NULL,
    start  INT          NOT NULL,
    end    INT          NOT NULL,
    `path` VARCHAR(255) NULL,
    error  VARCHAR(255) NULL,
    `status` ENUM('PENDING','COMPLETED','FAILED') NOT NULL,
    CONSTRAINT pk_downloadjob PRIMARY KEY (id)
);