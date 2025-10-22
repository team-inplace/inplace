package my.inplace.domain.user.query;

public class UserQueryResult {

    public record Info(
        String nickname,
        String imgUrl,
        String tierName,
        String tierImgUrl,
        String mainBadgeName,
        String mainBadgeImgUrl
    ) {

    }

    public record Badge(
        Long id,
        String name,
        String imgUrl,
        String condition
    ) {

    }

    public record BadgeWithOwnerShip(
        Long id,
        String name,
        String imgUrl,
        String description,
        Boolean isOwned,
        Boolean isSelected
    ) {

    }
}
