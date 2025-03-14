INSERT INTO cool_videos (id, video_id, uuid, influencer_name, place_id, place_name, place_category)
VALUES
    (1, 1, 'CoolVideo1', 'CoolInfluencer1', 101, 'CoolPlace1', 'RESTAURANT'),
    (2, 2, 'CoolVideo2', 'CoolInfluencer2', 102, 'CoolPlace2', 'CAFE'),
    (3, 3, 'CoolVideo3', 'CoolInfluencer3', 103, 'CoolPlace3', 'JAPANESE'),
    (4, 4, 'CoolVideo4', 'CoolInfluencer4', 104, 'CoolPlace4', 'KOREAN'),
    (5, 5, 'CoolVideo5', 'CoolInfluencer5', 105, 'CoolPlace5', 'NONE'),
    (6, 6, 'CoolVideo6', 'CoolInfluencer6', 106, 'CoolPlace6', 'WESTERN'),
    (7, 7, 'CoolVideo7', 'CoolInfluencer7', 107, 'CoolPlace7', 'RESTAURANT'),
    (8, 8, 'CoolVideo8', 'CoolInfluencer8', 108, 'CoolPlace8', 'CAFE'),
    (9, 9, 'CoolVideo9', 'CoolInfluencer9', 109, 'CoolPlace9', 'JAPANESE'),
    (10, 10, 'CoolVideo10', 'CoolInfluencer10', 110, 'CoolPlace10', 'KOREAN');

INSERT INTO places (id, name, latitude, longitude, address1, address2, address3, category)
VALUES (1, 'testPlace1', 36.90, 126.90, 'add1', 'add2', 'add3', 'RESTAURANT'),
       (2, 'testPlace2', 36.91, 126.91, 'add1', 'add2', 'add3', 'CAFE'),
       (3, 'testPlace3', 36.92, 126.92, 'add1', 'add2', 'add3', 'WESTERN'),
       (4, 'testPlace4', 36.93, 126.93, 'add1', 'add2', 'add3', 'JAPANESE'),
       (5, 'testPlace5', 36.94, 126.94, 'add1', 'add2', 'add3', 'KOREAN'),
       (6, 'testPlace6', 36.95, 126.95, 'add1', 'add2', 'add3', 'RESTAURANT'),
       (7, 'testPlace7', 36.96, 126.96, 'add1', 'add2', 'add3', 'CAFE'),
       (8, 'testPlace8', 36.97, 126.97, 'add1', 'add2', 'add3', 'WESTERN'),
       (9, 'testPlace9', 36.98, 126.98, 'add1', 'add2', 'add3', 'JAPANESE'),
       (10, 'testPlace10', 36.99, 126.99, 'add1', 'add2', 'add3', 'KOREAN'),
       (11, 'testPlace11', 37.00, 127.00, 'add1', 'add2', 'add3', 'RESTAURANT'),
       (12, 'testPlace12', 37.01, 127.01, 'add1', 'add2', 'add3', 'CAFE'),
       (13, 'testPlace13', 37.02, 127.02, 'add1', 'add2', 'add3', 'WESTERN'),
       (14, 'testPlace14', 37.03, 127.03, 'add1', 'add2', 'add3', 'JAPANESE'),
       (15, 'testPlace15', 37.04, 127.04, 'add1', 'add2', 'add3', 'KOREAN'),
       (16, 'testPlace16', 37.05, 127.05, 'add1', 'add2', 'add3', 'RESTAURANT'),
       (17, 'testPlace17', 37.06, 127.06, 'add1', 'add2', 'add3', 'CAFE'),
       (18, 'testPlace18', 37.07, 127.07, 'add1', 'add2', 'add3', 'WESTERN'),
       (19, 'testPlace19', 37.08, 127.08, 'add1', 'add2', 'add3', 'JAPANESE'),
       (20, 'testPlace20', 37.09, 127.09, 'add1', 'add2', 'add3', 'KOREAN');

INSERT INTO influencers (id, name, job, channel_id, img_url)
VALUES (1, 'influencer1', 'job1', 'channel1', 'img1'),
       (2, 'influencer2', 'job2', 'channel2', 'img2'),
       (3, 'influencer3', 'job3', 'channel3', 'img3'),
       (4, 'influencer4', 'job4', 'channel4', 'img4'),
       (5, 'influencer5', 'job5', 'channel5', 'img5');

INSERT INTO videos (id, influencer_id, place_id, uuid, view_count_increase, publish_time)
VALUES (1, 1, 1, 'Video1', 1, '2025-02-06 12:00:01.000000'),
       (2, 1, 2, 'Video2', 2, '2025-02-06 12:01:01.000000'),
       (3, 1, 3, 'Video3', 3, '2025-02-06 12:02:01.000000'),
       (4, 1, 4, 'Video4', 4, '2025-02-06 12:03:01.000000'),
       (5, 2, 5, 'Video5', 5, '2025-02-06 12:04:01.000000'),
       (6, 2, 6, 'Video6', 6, '2025-02-06 12:05:01.000000'),
       (7, 2, 7, 'Video7', 7, '2025-02-06 12:06:01.000000'),
       (8, 2, 8, 'Video8', 8, '2025-02-06 12:07:01.000000'),
       (9, 3, 9, 'Video9', 9, '2025-02-06 12:08:01.000000'),
       (10, 3, 10, 'Video10', 10, '2025-02-06 12:09:01.000000'),
       (11, 3, 11, 'Video11', 11, '2025-02-06 12:10:01.000000'),
       (12, 3, 12, 'Video12', 12, '2025-02-06 12:11:01.000000'),
       (13, 4, 13, 'Video13', 13, '2025-02-06 12:12:01.000000'),
       (14, 4, 14, 'Video14', 14, '2025-02-06 12:13:01.000000'),
       (15, 4, 15, 'Video15', 15, '2025-02-06 12:14:01.000000'),
       (16, 4, 16, 'Video16', 16, '2025-02-06 12:15:01.000000'),
       (17, 5, 17, 'Video17', 17, '2025-02-06 12:16:01.000000'),
       (18, 5, 18, 'Video18', 18, '2025-02-06 12:17:01.000000'),
       (19, 5, 19, 'Video19', 19, '2025-02-06 12:18:01.000000'),
       (20, 5, 20, 'Video20', 20, '2025-02-06 12:19:01.000000'),
       (21, 5, null, 'Video21', 16, '2025-02-06 12:20:01.000000'),
       (22, 5, null, 'Video22', 17, '2025-02-06 12:21:01.000000'),
       (23, 5, null, 'Video23', 18, '2025-02-06 12:22:01.000000'),
       (24, 5, null, 'Video24', 19, '2025-02-06 12:23:01.000000'),
       (25, 5, null, 'Video25', 20, '2025-02-06 12:24:01.000000');

INSERT INTO recent_videos (id, video_id, uuid, influencer_name, place_id, place_name, place_category)
VALUES
    (1, 1, 'RecentVideo1', 'RecentInfluencer1', 101, 'RecentPlace1', 'RESTAURANT'),
    (2, 2, 'RecentVideo2', 'RecentInfluencer2', 102, 'RecentPlace2', 'CAFE'),
    (3, 3, 'RecentVideo3', 'RecentInfluencer3', 103, 'RecentPlace3', 'JAPANESE'),
    (4, 4, 'RecentVideo4', 'RecentInfluencer4', 104, 'RecentPlace4', 'KOREAN'),
    (5, 5, 'RecentVideo5', 'RecentInfluencer5', 105, 'RecentPlace5', 'NONE'),
    (6, 6, 'RecentVideo6', 'RecentInfluencer6', 106, 'RecentPlace6', 'WESTERN'),
    (7, 7, 'RecentVideo7', 'RecentInfluencer7', 107, 'RecentPlace7', 'RESTAURANT'),
    (8, 8, 'RecentVideo8', 'RecentInfluencer8', 108, 'RecentPlace8', 'CAFE'),
    (9, 9, 'RecentVideo9', 'RecentInfluencer9', 109, 'RecentPlace9', 'JAPANESE'),
    (10, 10, 'RecentVideo10', 'RecentInfluencer10', 110, 'RecentPlace10', 'KOREAN');
