INSERT INTO users (id, username, nickname)
VALUES (1, '유저1', 'user_1'),
       (2, '유저2', 'user_2'),
       (3, '유저3', 'user_3'),
       (4, '유저4', 'user_4');

INSERT INTO posts (id, title, content, author_id, image_infos, delete_at)
VALUES (1, '첫 번째 게시글', '첫 번째 게시글 내용', 1,
        '[
            {
                \"imageUrl\": \"https://example.com/image1.jpg\",
                \"imageHash\": \"abc123\"
            }
        ]',
        NULL),
       (2, '두 번째 게시글', '두 번째 게시글 내용', 1,
        '[
            {
                \"imageUrl\": \"https://example.com/image2.jpg\",
                \"imageHash\": \"def456\"
            }
        ]',
        NULL),
       (3, '세 번째 게시글', '세 번째 게시글 내용', 2,
        '[
            {
                \"imageUrl\": \"https://example.com/image3.jpg\",
                \"imageHash\": \"ghi789\"
            }
        ]',
        CURRENT_TIMESTAMP),
       (4, '네 번째 게시글', '네 번째 게시글 내용', 2,
        '[
            {
                \"imageUrl\": \"https://example.com/image4.jpg\",
                \"imageHash\": \"jkl012\"
            }
        ]',
        NULL),
       (5, '다섯 번째 게시글', '다섯 번째 게시글 내용', 3,
        '[
            {
                \"imageUrl\": \"https://example.com/image5.jpg\",
                \"imageHash\": \"mno345\"
            }
        ]',
        NULL),
       (6, '여섯 번째 게시글', '여섯 번째 게시글 내용', 3,
        '[
            {
                \"imageUrl\": \"https://example.com/image6.jpg\",
                \"imageHash\": \"pqr678\"
            }
        ]',
        CURRENT_TIMESTAMP),
       (7, '일곱 번째 게시글', '일곱 번째 게시글 내용', 4,
        '[
            {
                \"imageUrl\": \"https://example.com/image7.jpg\",
                \"imageHash\": \"stu901\"
            }
        ]',
        NULL),
       (8, '여덟 번째 게시글', '여덟 번째 게시글 내용', 4,
        '[
            {
                \"imageUrl\": \"https://example.com/image8.jpg\",
                \"imageHash\": \"vwx234\"
            }
        ]',
        NULL);

INSERT INTO comments (id, content, author_id, post_id)
VALUES (1, '첫 번째 댓글', 2, 1),
       (2, '두 번째 댓글', 3, 1),
       (3, '세 번째 댓글', 4, 2),
       (4, '네 번째 댓글', 1, 2),
       (5, '다섯 번째 댓글', 1, 3),
       (6, '여섯 번째 댓글', 2, 3),
       (7, '일곱 번째 댓글', 3, 4),
       (8, '여덟 번째 댓글', 4, 4);