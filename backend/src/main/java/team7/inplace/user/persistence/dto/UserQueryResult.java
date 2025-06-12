package team7.inplace.user.persistence.dto;

import com.querydsl.core.annotations.QueryProjection;

public class UserQueryResult {

    public record Simple(
        String nickname,
        String imgUrl,
        String tierName,
        String tierImgUrl,
        String mainBadgeName,
        String mainBadgeImgUrl
    ) {

        @QueryProjection
        public Simple {

        }
    }

    public record Badge(
        Long id,
        String name,
        String imgUrl,
        String condition
    ) {
        @QueryProjection
        public Badge {}

    }
}
