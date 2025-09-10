package my.inplace.application.place.query.dto;

import java.util.List;
import my.inplace.domain.place.query.PlaceQueryParam;

public class PlaceParam {

    public record Coordinate(
        Double topLeftLongitude,
        Double topLeftLatitude,
        Double bottomRightLongitude,
        Double bottomRightLatitude,
        Double longitude,
        Double latitude
    ) {

        public PlaceQueryParam.Coordinate toQueryParam() {
            return new PlaceQueryParam.Coordinate(
                topLeftLongitude,
                topLeftLatitude,
                bottomRightLongitude,
                bottomRightLatitude,
                longitude,
                latitude
            );
        }
    }

    public record Filter(
        List<PlaceParam.Region> regions,
        List<Long> categories,
        List<String> influencers
    ) {

        public PlaceQueryParam.Filter toQueryParam() {
            return new PlaceQueryParam.Filter(
                regions.stream().map(PlaceParam.Region::toQueryParam).toList(),
                categories,
                influencers
            );
        }
    }

    public record Region(
        String city,
        String district
    ) {

        public PlaceQueryParam.Region toQueryParam() {
            return new PlaceQueryParam.Region(city, district);
        }
    }
}
