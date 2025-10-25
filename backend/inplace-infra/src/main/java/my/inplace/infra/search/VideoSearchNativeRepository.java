package my.inplace.infra.search;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.inplace.domain.search.SearchRepository;
import my.inplace.domain.video.query.VideoQueryResult.DetailedVideo;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@Primary
@Slf4j
@RequiredArgsConstructor
public class VideoSearchNativeRepository implements SearchRepository<DetailedVideo> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public Page<DetailedVideo> search(String keyword, Pageable pageable, Long userId) {
        String countSql = """
            SELECT COUNT(video_id)
            FROM (
                SELECT v.id AS video_id
                FROM videos v
                    INNER JOIN influencers i ON v.influencer_id = i.id
                    INNER JOIN place_videos pv ON pv.video_id = v.id
                    INNER JOIN places p ON p.id = pv.place_id
                WHERE MATCH(i.name) AGAINST(:keyword IN BOOLEAN MODE) > 0
            
                UNION ALL
            
                SELECT v.id AS video_id
                FROM videos v
                    INNER JOIN place_videos pv ON pv.video_id = v.id
                    INNER JOIN places p ON p.id = pv.place_id
                WHERE MATCH(p.name) AGAINST(:keyword IN BOOLEAN MODE) > 0
            ) as results;
            """;

        Object result = entityManager.createNativeQuery(countSql)
            .setParameter("keyword", keyword)
            .getSingleResult();

        long count = (result != null) ? ((Number) result).longValue() : 0L;

        if (count == 0) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        String searchSql = """
            SELECT video_id, video_uuid, influencer_name, place_id, place_name,
                   parent_category_eng_name, category_parent_id,
                   place_address1, place_address2, place_address3
            FROM (
                SELECT v.id as video_id, v.uuid as video_uuid,
                       i.name as influencer_name, p.id as place_id, p.name as place_name,
                       pc.eng_name as parent_category_eng_name, c.parent_id as category_parent_id,
                       p.address1 as place_address1, p.address2 as place_address2, p.address3 as place_address3,
                       MATCH(p.name) AGAINST(:keyword IN BOOLEAN MODE) AS place_score,
                       0 AS influencer_score
                FROM videos v
                    INNER JOIN place_videos pv ON v.id = pv.video_id
                    INNER JOIN places p ON p.id = pv.place_id
                    INNER JOIN influencers i ON v.influencer_id = i.id
                    INNER JOIN categories c ON p.category_id = c.id
                    LEFT JOIN categories pc ON c.parent_id = pc.id
                WHERE MATCH(p.name) AGAINST(:keyword IN BOOLEAN MODE) > 0
                UNION ALL
                SELECT v.id as video_id, v.uuid as video_uuid,
                       i.name as influencer_name, p.id as place_id, p.name as place_name,
                       pc.eng_name as parent_category_eng_name, c.parent_id as category_parent_id,
                       p.address1 as place_address1, p.address2 as place_address2, p.address3 as place_address3,
                       0 AS place_score,
                       MATCH(i.name) AGAINST(:keyword IN BOOLEAN MODE) AS influencer_score
                FROM videos v
                    INNER JOIN influencers i ON v.influencer_id = i.id
                    INNER JOIN place_videos pv ON v.id = pv.video_id
                    INNER JOIN places p ON p.id = pv.place_id
                    INNER JOIN categories c ON p.category_id = c.id
                    LEFT JOIN categories pc ON c.parent_id = pc.id
                WHERE MATCH(i.name) AGAINST(:keyword IN BOOLEAN MODE) > 0
            ) results
            GROUP BY video_id, video_uuid, influencer_name, place_id, place_name,
                     parent_category_eng_name, category_parent_id,
                     place_address1, place_address2, place_address3
            ORDER BY MAX(place_score) DESC, MAX(influencer_score) DESC
            LIMIT :limit OFFSET :offset;
            """;

        List<DetailedVideo> contents = entityManager.createNativeQuery(searchSql, DetailedVideo.class)
            .setParameter("keyword", keyword)
            .setParameter("limit", pageable.getPageSize())
            .setParameter("offset", pageable.getOffset())
            .getResultList();

        return new PageImpl<>(contents, pageable, count);
    }
}
