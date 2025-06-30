INSERT INTO users (id, username, nick_name, tier_id, main_badge_id)
VALUES (1, '유저1', 'user_1', 1, 2),
       (2, '유저2', 'user_2', 1, 1),
       (3, '유저3', 'user_3', 2, 3),
       (4, '유저4', 'user_4', 2, 1);

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

INSERT INTO comments (id, content, author_id, post_id)
VALUES (1, '첫 번째 댓글', 2, 1),
       (2, '두 번째 댓글', 3, 1),
       (3, '세 번째 댓글', 4, 2),
       (4, '네 번째 댓글', 1, 2),
       (5, '다섯 번째 댓글', 1, 3),
       (6, '여섯 번째 댓글', 2, 3),
       (7, '일곱 번째 댓글', 3, 4),
       (8, '여덟 번째 댓글', 4, 4);
