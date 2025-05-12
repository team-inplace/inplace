INSERT INTO categories(id, name, parent_id)
VALUES (1, '맛집', null),
       (2, '카페', 1);

INSERT INTO places (id, name, latitude, longitude, address1, address2, address3, category_id)
VALUES (1, '테스트장소1', 36.0, 126.0, '주소1', '주소2', '주소3', 1),
       (2, '테스트장소2', 36.1, 126.1, '주소1', '주소2', '주소3', 2);

INSERT INTO users(id, nickname, username, role, user_type)
VALUES (1, 'user1', 'user1@mail', 'USER', 'KAKAO'),
       (2, 'user2', 'user2@mail', 'USER', 'KAKAO'),
       (3, 'user3', 'user3@mail', 'USER', 'KAKAO'),
       (4, 'user4', 'user4@mail', 'USER', 'KAKAO'),
       (5, 'user5', 'user5@mail', 'USER', 'KAKAO');

INSERT INTO reviews (id, place_id, user_id, is_liked, comment)
VALUES (1, 1, 1, true, '1->1 like'),
       (2, 1, 2, true, '2->1 like'),
       (3, 1, 3, false, '3->1 dislike'),
       (4, 1, 4, false, '4->1 dislike'),
       (5, 1, 5, false, '5->1 dislike'),
       (6, 2, 1, true, '1->2 like'),
       (7, 2, 2, true, '2->2 like'),
       (8, 2, 3, true, '3->2 like'),
       (9, 2, 4, true, '4->2 like'),
       (10, 2, 5, true, '5->2 like');
