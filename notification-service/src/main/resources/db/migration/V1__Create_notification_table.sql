CREATE TABLE notification
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    user_id    BIGINT                NULL,
    message    VARCHAR(255)          NULL,
    created_at datetime              NULL,
    CONSTRAINT pk_notification PRIMARY KEY (id)
);