package my.inplace.domain.place.query;

import java.util.List;

public class PlaceQueryParam {

    public record Coordinate(
        Double topLeftLongitude,
        Double topLeftLatitude,
        Double bottomRightLongitude,
        Double bottomRightLatitude
    ) {

    }

    public record Filter(
        List<Long> regions,
        List<Long> categories,
        List<String> influencers
    ) {

    }
}
