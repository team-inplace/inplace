package my.inplace.application.place.query.dto;

import java.util.List;
import my.inplace.domain.place.query.PlaceQueryParam;

public class PlaceParam {

    public record Coordinate(
        Double topLeftLongitude,
        Double topLeftLatitude,
        Double bottomRightLongitude,
        Double bottomRightLatitude
    ) {

        public PlaceQueryParam.Coordinate toQueryParam() {
            return new PlaceQueryParam.Coordinate(
                topLeftLongitude,
                topLeftLatitude,
                bottomRightLongitude,
                bottomRightLatitude
            );
        }
    }

    public record Filter(
        List<Long> regions,
        List<Long> categories,
        List<String> influencers
    ) {

        public PlaceQueryParam.Filter toQueryParam() {
            return new PlaceQueryParam.Filter(
                regions,
                categories,
                influencers
            );
        }
    }
}
