package team7.inplace.place.application.dto;

import java.util.List;
import team7.inplace.place.persistence.dto.PlaceQueryResult;
import team7.inplace.review.persistence.dto.ReviewQueryResult;
import team7.inplace.video.persistence.dto.VideoQueryResult;
import team7.inplace.video.persistence.dto.VideoQueryResult.SimpleVideo;

public class PlaceQueryInfo {
    public record Detail(
            PlaceQueryResult.DetailedPlaceBulk placeBulk,
            List<SimpleVideo> videos,
            ReviewQueryResult.LikeRate reviewLikeRate
    ) {
        public static PlaceQueryInfo.Detail from(
                PlaceQueryResult.DetailedPlaceBulk placeBulk,
                List<VideoQueryResult.SimpleVideo> video,
                ReviewQueryResult.LikeRate reviewLikeRate
        ) {
            return new PlaceQueryInfo.Detail(placeBulk, video, reviewLikeRate);
        }
    }

    public record Simple(
            PlaceQueryResult.DetailedPlace place,
            List<SimpleVideo> video
    ) {
        public static PlaceQueryInfo.Simple from(
                PlaceQueryResult.DetailedPlace place,
                List<VideoQueryResult.SimpleVideo> video
        ) {
            return new PlaceQueryInfo.Simple(place, video);
        }
    }

    public record Marker(
        PlaceQueryResult.Marker place,
        String influencerNames
    ) {
        public static PlaceQueryInfo.Marker from(PlaceQueryResult.Marker marker, List<String> influencerNames) {
            return new PlaceQueryInfo.Marker(marker, String.join(",", influencerNames));
        }
    }
}
