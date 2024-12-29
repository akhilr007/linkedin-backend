CREATE TABLE posts
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    content    VARCHAR(255)          NOT NULL,
    user_id    BIGINT                NOT NULL,
    created_at datetime              NULL,
    CONSTRAINT pk_posts PRIMARY KEY (id)
);