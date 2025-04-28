package team7.inplace.place.application.dto;

import java.util.List;
import team7.inplace.place.client.GooglePlaceClientResponse;
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
        ReviewQueryResult.LikeRate reviewLikeRate
    ) {

        public static Detail of(
            PlaceQueryResult.DetailedPlace place, GooglePlaceClientResponse.Place googlePlace,
            List<VideoQueryResult.SimpleVideo> videos, ReviewQueryResult.LikeRate reviewLikeRate
        ) {
            return new Detail(
                place,
                googlePlace,
                videos,
                reviewLikeRate
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
        String name
    ) {

    }
}
