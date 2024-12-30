CREATE TABLE post_likes
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    post_id    BIGINT                NOT NULL,
    user_id    BIGINT                NOT NULL,
    created_at datetime              NULL,
    CONSTRAINT pk_post_likes PRIMARY KEY (id)
);