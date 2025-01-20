package team7.inplace.influencer.persistence.dto;

import com.querydsl.core.annotations.QueryProjection;

public class InfluencerQueryResult {
    public record Simple(
            Long id,
            String name,
            String imgUrl,
            String job,
            boolean isLiked
    ) {
        @QueryProjection
        public Simple {
        }
    }
}