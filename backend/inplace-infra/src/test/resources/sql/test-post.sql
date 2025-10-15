INSERT INTO users (id, username, nickname, tier_id, main_badge_id, profile_image_url)
VALUES (1, '유저1', 'nickname1', 1, 2, 'img_url1'),
       (2, '유저2', 'nickname2', 1, 1, 'img_url2'),
       (3, '유저3', 'nickname3', 2, 3, 'img_url3'),
       (4, '유저4', 'nickname4', 2, 1, 'img_url4');

INSERT INTO user_tiers (id, name, eng_name, required_posts, required_comments, required_likes, img_url)
VALUES  (1, '브론즈', 'BRONZE', 0, 0, 0, 'bronze.png'),
        (2, '실버', 'SILVER', 10, 5, 20, 'silver.png');

INSERT INTO badges (id, conditions, name, img_url)
VALUES  (1, '5개 글 작성', '글쟁이', 'badge1.png'),
        (2, '10개 댓글 작성', '수다쟁이', 'badge2.png'),
        (3, '50개 좋아요', '인기인', 'badge3.png');

INSERT INTO user_badges (id, user_id, badge_id)
VALUES  (1, 1, 1), -- 유저1 → 글쟁이
        (2, 1, 2), -- 유저1 → 수다쟁이
        (3, 2, 2), -- 유저2 → 수다쟁이
        (4, 2, 3), -- 유저2 → 인기인
        (5, 3, 1), -- 유저3 → 글쟁이
        (6, 3, 3), -- 유저3 → 인기인
        (7, 4, 1);

INSERT INTO posts (id, title, content, author_id, image_infos, created_at, total_like_count, total_comment_count)
VALUES (1, '첫 번째 게시글', '첫 번째 게시글 내용', 1,
        '[
            {
                \"imageUrl\": \"https://example.com/image1.jpg\",
                \"imageHash\": \"abc123\"
            }
        ]',
        '2025-09-01T00:00:00', 2, 2
       ),
       (2, '두 번째 게시글', '두 번째 게시글 내용', 1,
        '[
            {
                \"imageUrl\": \"https://example.com/image2.jpg\",
                \"imageHash\": \"def456\"
            }
        ]',
        '2025-09-01T00:00:01', 2, 2
       ),
       (3, '세 번째 게시글', '세 번째 게시글 내용', 2,
        '[
            {
                \"imageUrl\": \"https://example.com/image3.jpg\",
                \"imageHash\": \"ghi789\"
            }
        ]',
        '2025-09-01T00:00:02', 2, 2
       ),
       (4, '네 번째 게시글', '네 번째 게시글 내용', 2,
        '[
            {
                \"imageUrl\": \"https://example.com/image4.jpg\",
                \"imageHash\": \"jkl012\"
            }
        ]',
        '2025-09-01T00:00:03', 1, 2
       ),
       (5, '다섯 번째 게시글', '다섯 번째 게시글 내용', 3,
        '[
            {
                \"imageUrl\": \"https://example.com/image5.jpg\",
                \"imageHash\": \"mno345\"
            }
        ]',
        '2025-09-01T00:00:04', 2, 0
       ),
       (6, '여섯 번째 게시글', '여섯 번째 게시글 내용', 3,
        '[
            {
                \"imageUrl\": \"https://example.com/image6.jpg\",
                \"imageHash\": \"pqr678\"
            }
        ]',
        '2025-09-01T00:00:05', 2, 0
       ),
       (7, '일곱 번째 게시글', '일곱 번째 게시글 내용', 4,
        '[
            {
                \"imageUrl\": \"https://example.com/image7.jpg\",
                \"imageHash\": \"stu901\"
            }
        ]',
        '2025-09-01T00:00:06', 1, 0
       ),
       (8, '여덟 번째 게시글', '여덟 번째 게시글 내용', 4,
        '[
            {
                \"imageUrl\": \"https://example.com/image8.jpg\",
                \"imageHash\": \"vwx234\"
            }
        ]',
        '2025-09-01T00:00:07', 1, 0
       );

INSERT INTO comments (id, content, author_id, post_id, created_at, total_like_count)
VALUES (1, '첫 번째 댓글', 2, 1, '2025-09-01T00:00:01', 1),
       (2, '두 번째 댓글', 3, 1, '2025-09-01T00:00:02', 1),
       (3, '세 번째 댓글', 4, 2, '2025-09-01T00:00:03', 1),
       (4, '네 번째 댓글', 1, 2, '2025-09-01T00:00:04', 1),
       (5, '다섯 번째 댓글', 1, 3, '2025-09-01T00:00:05', 1),
       (6, '여섯 번째 댓글', 2, 3, '2025-09-01T00:00:06', 1),
       (7, '일곱 번째 댓글', 3, 4, '2025-09-01T00:00:07', 1),
       (8, '여덟 번째 댓글', 4, 4, '2025-09-01T00:00:08', 2);

INSERT INTO liked_comments (id, comment_id, user_id, is_liked) VALUES
       (1, 3, 1, true),
       (2, 5, 2, true),
       (3, 3, 2, false),
       (4, 8, 4, true),
       (5, 1, 3, false),
       (6, 7, 1, true),
       (7, 2, 4, true),
       (8, 6, 2, true),
       (9, 4, 3, true),
       (10, 5, 1, false),
       (11, 8, 3, true),
       (12, 1, 4, true);

INSERT INTO liked_posts (id, post_id, user_id, is_liked) VALUES
         (1, 5, 2, true),
         (2, 8, 1, false),
         (3, 3, 3, true),
         (4, 1, 4, true),
         (5, 7, 2, false),
         (6, 2, 1, true),
         (7, 6, 4, true),
         (8, 4, 3, false),
         (9, 5, 1, true),
         (10, 8, 2, true),
         (11, 1, 3, true),
         (12, 7, 4, false),
         (13, 2, 2, false),
         (14, 6, 3, true),
         (15, 4, 1, true),
         (16, 8, 4, false),
         (17, 3, 2, true),
         (18, 5, 3, false),
         (19, 2, 4, true),
         (20, 7, 1, true);
