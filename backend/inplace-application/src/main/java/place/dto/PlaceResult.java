package place.dto;

import java.util.List;
import place.client.GooglePlaceClientResponse;
import place.query.PlaceQueryResult;
import place.query.PlaceQueryResult.MarkerDetail;
import review.query.ReviewQueryResult;
import video.query.VideoQueryResult;
import video.query.VideoQueryResult.SimpleVideo;

public class PlaceResult {

    public record Detail(
        PlaceQueryResult.DetailedPlace place,
        GooglePlaceClientResponse.Place googlePlace,
        List<VideoQueryResult.SimpleVideo> videos,
        ReviewQueryResult.LikeRate reviewLikeRate,
        List<VideoQueryResult.DetailedVideo> surroundVideos
    ) {

        public static Detail of(
            PlaceQueryResult.DetailedPlace place,
            GooglePlaceClientResponse.Place googlePlace,
            List<VideoQueryResult.SimpleVideo> videos,
            ReviewQueryResult.LikeRate reviewLikeRate,
            List<VideoQueryResult.DetailedVideo> surroundVideos
        ) {
            return new Detail(
                place,
                googlePlace,
                videos,
                reviewLikeRate,
                surroundVideos
            );
        }
    }

    public record Simple(
        PlaceQueryResult.DetailedPlace place,
        List<SimpleVideo> video
    ) {

        public static PlaceResult.Simple of(
            PlaceQueryResult.DetailedPlace place,
            List<VideoQueryResult.SimpleVideo> video
        ) {
            return new PlaceResult.Simple(place, video);
        }
    }

    public record Marker(
        MarkerDetail place,
        List<VideoQueryResult.SimpleVideo> videos
    ) {

        public static PlaceResult.Marker of(
            MarkerDetail markerDetail, List<VideoQueryResult.SimpleVideo> videos) {
            return new PlaceResult.Marker(markerDetail, videos);
        }
    }

    public record Category(
        Long id,
        Long parentId,
        String name,
        String engName
    ) {

        public static Category from(place.Category category) {
            return new Category(
                category.getId(),
                category.getParentId(),
                category.getName(),
                category.getEngName()
            );
        }
    }
}
