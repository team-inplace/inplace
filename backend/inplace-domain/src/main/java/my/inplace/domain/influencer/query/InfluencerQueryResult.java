package my.inplace.domain.influencer.query;

public class InfluencerQueryResult {

    public record Simple(
        Long id,
        String name,
        String imgUrl,
        String job,
        boolean isLiked
    ) {

    }

    public record Detail(
        Long id,
        String name,
        String imgUrl,
        String job,
        Boolean isLiked,
        Long follower,
        Long placeCount
    ) {

    }
}
