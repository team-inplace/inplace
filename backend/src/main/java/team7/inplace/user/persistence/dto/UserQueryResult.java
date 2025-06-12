package team7.inplace.user.persistence.dto;

import com.querydsl.core.annotations.QueryProjection;

public class UserQueryResult {

    public record Info(
        Long userId,
        String nickname,
        String imgUrl,
        String badgeImgUrl,
        String tierImgUrl
    ) {

        @QueryProjection
        public Info {

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
