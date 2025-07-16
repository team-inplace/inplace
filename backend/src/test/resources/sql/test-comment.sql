insert into users (id, username, nickname, profile_image_url)
    VALUE (1, 'test', 'user1', 'https://example.com/profile.jpg'),
    (2, 'test2', 'user2', 'https://example.com/profile2.jpg'),
    (3, 'test3', 'user3', 'https://example.com/profile3.jpg'),
    (4, 'test4', 'user4', 'https://example.com/profile4.jpg'),
    (5, 'test5', 'user5', 'https://example.com/profile5.jpg'),
    (6, 'test6', 'user6', 'https://example.com/profile6.jpg'),
    (7, 'test7', 'user7', 'https://example.com/profile7.jpg'),
    (8, 'test8', 'user8', 'https://example.com/profile8.jpg'),
    (9, 'test9', 'user9', 'https://example.com/profile9.jpg'),
    (10, 'test10', 'user10', 'https://example.com/profile10.jpg');

INSERT INTO posts (ID, TITLE, CONTENT, AUTHOR_ID, CREATED_AT)
VALUES (1, 'Test', 'Test', 1, CURRENT_TIMESTAMP);

INSERT INTO comments (ID, POST_ID, CONTENT, AUTHOR_ID, CREATED_AT)
VALUES (1, 1, 'Test comment', 1, CURRENT_TIMESTAMP),
       (2, 1, 'Test comment 2', 2, CURRENT_TIMESTAMP),
       (3, 1, 'Test comment 3', 3, CURRENT_TIMESTAMP),
       (4, 1, 'Test comment 4', 4, CURRENT_TIMESTAMP),
       (5, 1, 'Test comment 5', 5, CURRENT_TIMESTAMP),
       (6, 1, 'Test comment 6', 6, CURRENT_TIMESTAMP),
       (7, 1, 'Test comment 7', 7, CURRENT_TIMESTAMP),
       (8, 1, 'Test comment 8', 8, CURRENT_TIMESTAMP),
       (9, 1, 'Test comment 9', 9, CURRENT_TIMESTAMP),
       (10, 1, 'Test comment 10', 10, CURRENT_TIMESTAMP);

INSERT INTO liked_comments(ID, comment_id, user_id)
VALUES (1, 1, 1);