INSERT INTO influencers (id, name, job, channel_id, img_url, hidden)
VALUES (1, '인플루언서1', '직업1', 'channel1', 'img1', 0),
       (2, '인플루언서2', '직업2', 'channel2', 'img2', 0),
       (3, '인플루언서3', '직업3', 'channel3', 'img3', 0),
       (4, '인플루언서4', '직업4', 'channel4', 'img4', 0);

INSERT INTO categories(id, name, eng_name, parent_id)
VALUES (1, '맛집', 'eats', null),
       (2, '카페', 'cafe', 1),
       (3, '양식', 'western', 1),
       (4, '일식', 'japanese',1),
       (5, '한식', 'korean', 1);

INSERT INTO places (id, name, latitude, longitude, address1, address2, address3, category_id)
VALUES (1, '테스트장소1', 36.0, 126.0, '주소1', '주소2', '주소3', 1),
       (2, '테스트장소2', 36.1, 126.1, '주소1', '주소2', '주소3', 2),
       (3, '테스트장소3', 36.2, 126.2, '주소1', '주소2', '주소3', 3),
       (4, '테스트장소4', 36.3, 126.3, '주소1', '주소2', '주소3', 4);

INSERT INTO videos (id, influencer_id, place_id, uuid)
VALUES (1, 1, null, 'UUID1'),
       (2, 1, null, 'UUID2'),
       (3, 1, null, 'UUID3'),
       (4, 1, null, 'UUID4'),

       (5, 2, null, 'UUID5'),
       (6, 2, null, 'UUID6'),
       (7, 2, null, 'UUID7'),
       (8, 2, null, 'UUID8'),

       (9, 3, null, 'UUID9'),
       (10, 3, null, 'UUID10'),
       (11, 3, null, 'UUID11'),
       (12, 3, null, 'UUID12'),

       (13, 4, 1, 'UUID13'),
       (14, 4, 2, 'UUID14'),
       (15, 4, 3, 'UUID15'),
       (16, 4, 4, 'UUID16');
INSERT INTO place_videos(id, video_id, place_id)
VALUES (1, 13, 1),
       (2, 14, 2),
       (3, 15, 3),
       (4, 16, 4);
INSERT INTO users (id, username)
VALUES (1, '유저1'),
       (2, '유저2'),
       (3, '유저3'),
       (4, '유저4');

INSERT INTO liked_influencers (id, influencer_id, user_id, is_liked)
VALUES (1, 2, 1, TRUE),
       (2, 2, 2, TRUE),
       (3, 2, 3, TRUE),
       (4, 2, 4, TRUE),

       (5, 3, 1, true),
       (6, 3, 2, false),
       (7, 3, 3, true),
       (8, 3, 4, true),

       (9, 4, 1, true),
       (10, 4, 2, false),
       (11, 4, 3, false),
       (12, 4, 4, true);