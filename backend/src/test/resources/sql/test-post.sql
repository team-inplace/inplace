INSERT INTO users (id, username)
VALUES (1, '유저1'),
       (2, '유저2'),
       (3, '유저3'),
       (4, '유저4');

INSERT INTO posts (id, title, content, author_id, image_infos)
VALUES (1, '첫 번째 게시글', '첫 번째 게시글 내용', 1,
        '[
            {
                \"imageUrl\": \"https://example.com/image1.jpg\",
                \"imageHash\": \"abc123\"
            }
        ]'),
       (2, '두 번째 게시글', '두 번째 게시글 내용', 1,
        '[
            {
                \"imageUrl\": \"https://example.com/image2.jpg\",
                \"imageHash\": \"def456\"
            }
        ]'),
       (3, '세 번째 게시글', '세 번째 게시글 내용', 2,
        '[
            {
                \"imageUrl\": \"https://example.com/image3.jpg\",
                \"imageHash\": \"ghi789\"
            }
        ]'),
       (4, '네 번째 게시글', '네 번째 게시글 내용', 2,
        '[
            {
                \"imageUrl\": \"https://example.com/image4.jpg\",
                \"imageHash\": \"jkl012\"
            }
        ]'),
       (5, '다섯 번째 게시글', '다섯 번째 게시글 내용', 3,
        '[
            {
                \"imageUrl\": \"https://example.com/image5.jpg\",
                \"imageHash\": \"mno345\"
            }
        ]'),
       (6, '여섯 번째 게시글', '여섯 번째 게시글 내용', 3,
        '[
            {
                \"imageUrl\": \"https://example.com/image6.jpg\",
                \"imageHash\": \"pqr678\"
            }
        ]'),
       (7, '일곱 번째 게시글', '일곱 번째 게시글 내용', 4,
        '[
            {
                \"imageUrl\": \"https://example.com/image7.jpg\",
                \"imageHash\": \"stu901\"
            }
        ]'),
       (8, '여덟 번째 게시글', '여덟 번째 게시글 내용', 4,
        '[
            {
                \"imageUrl\": \"https://example.com/image8.jpg\",
                \"imageHash\": \"vwx234\"
            }
        ]');