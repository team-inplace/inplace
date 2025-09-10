package my.inplace.domain.place.query;

import java.util.List;

public class PlaceQueryParam {

    public record Coordinate(
        Double topLeftLongitude,
        Double topLeftLatitude,
        Double bottomRightLongitude,
        Double bottomRightLatitude,
        Double longitude,
        Double latitude
    ) {

    }

    public record Region(
        String city,
        String district
    ) {

    }

    public record Filter(
        List<Region> regions,
        List<Long> categories,
        List<String> influencers
    ) {

    }
}
