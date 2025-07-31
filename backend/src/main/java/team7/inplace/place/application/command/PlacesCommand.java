package team7.inplace.place.application.command;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import place.Place;
import team7.inplace.video.presentation.dto.VideoSearchParams;

@Slf4j
public class PlacesCommand {

    public record Coordinate(
        Double topLeftLongitude,
        Double topLeftLatitude,
        Double bottomRightLongitude,
        Double bottomRightLatitude,
        Double longitude,
        Double latitude
    ) {

        public static Coordinate from(
            VideoSearchParams videoSearchParams
        ) {
            return new Coordinate(
                Double.valueOf(videoSearchParams.topLeftLongitude()),
                Double.valueOf(videoSearchParams.topLeftLatitude()),
                Double.valueOf(videoSearchParams.bottomRightLatitude()),
                Double.valueOf(videoSearchParams.bottomRightLatitude()),
                Double.valueOf(videoSearchParams.longitude()),
                Double.valueOf(videoSearchParams.latitude()
                ));
        }
    }

    public record RegionParam(
        String city,
        String district
    ) {

    }

    public record FilterParams(
        List<RegionParam> regions,
        List<Long> categories,
        List<String> influencers
    ) {

    }

    public record Upsert(
        Long videoId,
        String placeName,
        Long category,
        String address,
        String x,
        String y,
        String googlePlaceId,
        Long kakaoPlaceId
    ) {

        public Place toEntity() {
            return new Place(
                placeName,
                category,
                address,
                x,
                y,
                googlePlaceId,
                kakaoPlaceId
            );
        }
    }
}
