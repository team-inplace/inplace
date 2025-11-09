CREATE FULLTEXT INDEX full_text_idx_place_name ON places (name) with parser ngram;

INSERT INTO categories(id, name, eng_name, parent_id)
VALUES (1, '맛집', 'eats', null),
       (2, '카페', 'cafe', 1),
       (3, '양식', 'western', 1),
       (4, '일식', 'japanese',1),
       (5, '한식', 'korean', 1);

INSERT INTO places (id, name, address1, address2, address3, google_place_id, kakao_place_id, category_id, location)
VALUES (1, '테스트장소1', '주소1-1', '주소2-1', '주소3', 'googlePlaceId1', 1, 2, ST_PointFromText('POINT(36.0 126.0)', 4326)),
       (2, '테스트장소2', '주소1-1', '주소2-1', '주소3', 'googlePlaceId2', 2, 2, ST_PointFromText('POINT(36.1 126.1)', 4326)),
       (3, '테스트장소3', '주소1-1', '주소2-1', '주소3', 'googlePlaceId3', 3, 3, ST_PointFromText('POINT(36.2 126.2)', 4326)),
       (4, '테스트장소4', '주소1-1', '주소2-1', '주소3', 'googlePlaceId4', 4, 4, ST_PointFromText('POINT(36.3 126.3)', 4326)),
       (5, '테스트장소5', '주소1-1', '주소2-1', '주소3', 'googlePlaceId5', 5, 5, ST_PointFromText('POINT(36.4 126.4)', 4326)),
       (6, '테스트장소6', '주소1-1', '주소2-2', '주소3', 'googlePlaceId6', 6, 2, ST_PointFromText('POINT(36.5 126.5)', 4326)),
       (7, '테스트장소7', '주소1-2', '주소2-2', '주소3', 'googlePlaceId7', 7, 2, ST_PointFromText('POINT(36.6 126.6)', 4326)),
       (8, '테스트장소8', '주소1-2', '주소2-2', '주소3', 'googlePlaceId8', 8, 3, ST_PointFromText('POINT(36.7 126.7)', 4326)),
       (9, '테스트장소9', '주소1-2', '주소2-3', '주소3', 'googlePlaceId9', 9, 4, ST_PointFromText('POINT(36.8 126.8)', 4326)),
       (10, '테스트장소10', '주소1-2', '주소2-3', '주소3', 'googlePlaceId10', 10, 5, ST_PointFromText('POINT(36.9 126.9)', 4326)),
       (11, '테스트장소11', '주소1', '주소2', '주소3', 'googlePlaceId11', 11, 2, ST_PointFromText('POINT(37.0 127.0)', 4326)),
       (12, '테스트장소12', '주소1', '주소2', '주소3', 'googlePlaceId12', 12, 2, ST_PointFromText('POINT(37.1 127.1)', 4326)),
       (13, '테스트장소13', '주소1', '주소2', '주소3', 'googlePlaceId13', 13, 3, ST_PointFromText('POINT(37.2 127.2)', 4326)),
       (14, '테스트장소14', '주소1', '주소2', '주소3', 'googlePlaceId14', 14, 4, ST_PointFromText('POINT(37.3 127.3)', 4326)),
       (15, '테스트장소15', '주소1', '주소2', '주소3', 'googlePlaceId15', 15, 5, ST_PointFromText('POINT(37.4 127.4)', 4326)),
       (16, '테스트장소16', '주소1', '주소2', '주소3', 'googlePlaceId16', 16, 2, ST_PointFromText('POINT(37.5 127.5)', 4326)),
       (17, '테스트장소17', '주소1', '주소2', '주소3', 'googlePlaceId17', 17, 2, ST_PointFromText('POINT(37.6 127.6)', 4326)),
       (18, '테스트장소18', '주소1', '주소2', '주소3', 'googlePlaceId18', 18, 3, ST_PointFromText('POINT(37.7 127.7)', 4326)),
       (19, '테스트장소19', '주소1', '주소2', '주소3', 'googlePlaceId19', 19, 4, ST_PointFromText('POINT(37.8 127.8)', 4326)),
       (20, '테스트장소20', '주소1', '주소2', '주소3', 'googlePlaceId20', 20, 5, ST_PointFromText('POINT(37.9 127.9)', 4326));

INSERT INTO influencers (id, name, job, channel_id, img_url)
VALUES (1, '인플루언서1', '직업1', 'channel1', 'img1'),
       (2, '인플루언서2', '직업2', 'channel2', 'img2'),
       (3, '인플루언서3', '직업3', 'channel3', 'img3'),
       (4, '인플루언서4', '직업4', 'channel4', 'img4'),
       (5, '인플루언서5', '직업5', 'channel5', 'img5');

INSERT INTO videos (id, influencer_id, place_id, uuid)
VALUES (1, 1, 1, 'Video1'),
       (2, 1, 2, 'Video2'),
       (3, 1, 3, 'Video3'),
       (4, 1, 4, 'Video4'),
       (5, 2, 5, 'Video5'),
       (6, 2, 6, 'Video6'),
       (7, 2, 7, 'Video7'),
       (8, 2, 8, 'Video8'),
       (9, 3, 9, 'Video9'),
       (10, 3, 10, 'Video10'),
       (11, 3, 11, 'Video11'),
       (12, 3, 12, 'Video12'),
       (13, 4, 13, 'Video13'),
       (14, 4, 14, 'Video14'),
       (15, 4, 15, 'Video15'),
       (16, 4, 16, 'Video16'),
       (17, 5, 17, 'Video17'),
       (18, 5, 18, 'Video18'),
       (19, 5, 19, 'Video19'),
       (20, 5, 20, 'Video20');


INSERT INTO place_videos (id, place_id, video_id)
VALUES (1, 1, 1),
       (2, 2, 2),
       (3, 3, 3),
       (4, 4, 4),
       (5, 5, 5),
       (6, 6, 6),
       (7, 7, 7),
       (8, 8, 8),
       (9, 9, 9),
       (10, 10, 10),
       (11, 11, 11),
       (12, 12, 12),
       (13, 13, 13),
       (14, 14, 14),
       (15, 15, 15),
       (16, 16, 16),
       (17, 17, 17),
       (18, 18, 18),
       (19, 19, 19),
       (20, 20, 20),
       (21, 20, 1);

INSERT INTO users (id, username)
VALUES (1, '유저1'),
       (2, '유저2'),
       (3, '유저3'),
       (4, '유저4');

INSERT INTO liked_places (id, place_id, user_id, is_liked)
VALUES (1, 15, 2, true),
    (2, 8, 4, true),
    (3, 19, 1, true),
    (4, 3, 3, true),
    (5, 11, 2, true),
    (6, 7, 4, true),
    (7, 20, 1, true),
    (8, 1, 3, true),
    (9, 14, 2, true),
    (10, 5, 4, true),
    (11, 18, 1, true),
    (12, 9, 3, true),
    (13, 2, 2, true),
    (14, 16, 4, true),
    (15, 12, 1, true),
    (16, 6, 3, true),
    (17, 17, 2, true),
    (18, 4, 4, true),
    (19, 13, 1, true),
    (20, 10, 3, true);
