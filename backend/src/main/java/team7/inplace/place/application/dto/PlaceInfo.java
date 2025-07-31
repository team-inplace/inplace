package team7.inplace.place.application.dto;

import java.util.List;
import place.GooglePlaceClientResponse;
import team7.inplace.place.persistence.dto.PlaceQueryResult;
import team7.inplace.place.persistence.dto.PlaceQueryResult.MarkerDetail;
import team7.inplace.review.persistence.dto.ReviewQueryResult;
import team7.inplace.video.persistence.dto.VideoQueryResult;
import team7.inplace.video.persistence.dto.VideoQueryResult.SimpleVideo;

public class PlaceInfo {

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

        public static PlaceInfo.Simple of(
            PlaceQueryResult.DetailedPlace place,
            List<VideoQueryResult.SimpleVideo> video
        ) {
            return new PlaceInfo.Simple(place, video);
        }
    }

    public record Marker(
        MarkerDetail place,
        List<VideoQueryResult.SimpleVideo> videos
    ) {

        public static PlaceInfo.Marker of(
            MarkerDetail markerDetail, List<VideoQueryResult.SimpleVideo> videos) {
            return new PlaceInfo.Marker(markerDetail, videos);
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
