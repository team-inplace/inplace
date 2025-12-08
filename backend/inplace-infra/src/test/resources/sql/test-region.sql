INSERT INTO regions(id, city, district, area)
VALUES (
        1, 'testCity1', 'testDist1', ST_GeometryFromText('POLYGON((0 0, 10 0, 0 10, 10 10, 0 0))', 4326)
       );
