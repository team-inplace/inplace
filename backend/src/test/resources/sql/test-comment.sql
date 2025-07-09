insert into users (id, username, nickname, profile_image_url)
    VALUE (1, 'test', 'user1', 'https://example.com/profile.jpg');

INSERT INTO posts (ID, TITLE, CONTENT, AUTHOR_ID, CREATED_AT)
VALUES (1, 'Test', 'Test', 1, CURRENT_TIMESTAMP);

INSERT INTO comments (ID, POST_ID, CONTENT, AUTHOR_ID, CREATED_AT)
VALUES (1, 1, 'Test comment', 1, CURRENT_TIMESTAMP),
       (2, 1, 'Test comment 2', 1, CURRENT_TIMESTAMP),
       (3, 1, 'Test comment 3', 1, CURRENT_TIMESTAMP),
       (4, 1, 'Test comment 4', 1, CURRENT_TIMESTAMP),
       (5, 1, 'Test comment 5', 1, CURRENT_TIMESTAMP),
       (6, 1, 'Test comment 6', 1, CURRENT_TIMESTAMP),
       (7, 1, 'Test comment 7', 1, CURRENT_TIMESTAMP),
       (8, 1, 'Test comment 8', 1, CURRENT_TIMESTAMP),
       (9, 1, 'Test comment 9', 1, CURRENT_TIMESTAMP),
       (10, 1, 'Test comment 10', 1, CURRENT_TIMESTAMP);

INSERT INTO liked_comments(ID, comment_id, user_id)
VALUES (1, 1, 1);