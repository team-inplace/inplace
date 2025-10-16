-- 1. user_tiers
INSERT INTO user_tiers (name, eng_name, required_posts, required_comments, required_likes, img_url) VALUES
                                                                                                        ( '브론즈', 'BRONZE', 0, 0, 0, 'bronze.png'),
                                                                                                        ( '실버', 'SILVER', 10, 5, 20, 'silver.png');

-- 2. users
INSERT INTO users (id, created_at, delete_at, update_at, nickname, password, username, role, user_type, profile_image_url, tier_id, main_badge_id, post_count, received_comment_count, received_like_count) VALUES
                                                                                                                                                                                                                (1, NOW(), NULL, NOW(), '유저1', 'pass1', 'user1@gmail.com', 'FIRST_USER', 'KAKAO', 'img1.png', 1, 1, 100, 2L, 10L),
                                                                                                                                                                                                                (2, NOW(), NULL, NOW(), '유저2', 'pass2', 'user2@gmail.com', 'FIRST_USER', 'KAKAO', 'img2.png', 2, 2, 1000, 10L, 110001L),
                                                                                                                                                                                                                (3, NOW(), NULL, NOW(), '유저3', 'pass3', 'user3@gmail.com', 'ADMIN', 'KAKAO', 'img3.png', 2, 3, 101, 123L, 1230L);

-- 3. badges
INSERT INTO badges (conditions, name, img_url) VALUES
                                                   ('5개 글 작성', '글쟁이', 'badge1.png'),
                                                   ('10개 댓글 작성', '수다쟁이', 'badge2.png'),
                                                   ('50개 좋아요', '인기인', 'badge3.png');

-- 4. user_badges
INSERT INTO user_badges (user_id, badge_id) VALUES
                                                (1, 1), -- 유저1 → 글쟁이
                                                (1, 2), -- 유저1 → 수다쟁이
                                                (2, 2), -- 유저2 → 수다쟁이
                                                (2, 3), -- 유저2 → 인기인
                                                (3, 1), -- 유저3 → 글쟁이
                                                (3, 3); -- 유저3 → 인기인
