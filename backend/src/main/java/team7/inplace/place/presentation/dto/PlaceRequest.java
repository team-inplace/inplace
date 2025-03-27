package team7.inplace.place.presentation.dto;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import team7.inplace.place.application.command.PlacesCommand;
import team7.inplace.place.application.command.PlacesCommand.FilterParams;
import team7.inplace.place.application.command.PlacesCommand.RegionParam;
import team7.inplace.place.domain.Category;

public class PlaceRequest {

    public record Like(
        Long placeId,
        Boolean likes
    ) {

    }

    public record Create(
        Long videoId,
        String placeName,
        String category,
        String address,
        String x,
        String y,
        Long kakaoPlaceId,
        String googlePlaceId
    ) {

        public PlacesCommand.Create toCommand() {
            return new PlacesCommand.Create(
                placeName, category, address, x, y, googlePlaceId, kakaoPlaceId
            );
        }
    }

    public record Coordinate(
        @NotNull(message = "longitude 값이 없습니다.") Double longitude,
        @NotNull(message = "latitude 값이 없습니다.") Double latitude,
        @NotNull(message = "topLeftLongitude 값이 없습니다.") Double topLeftLongitude,
        @NotNull(message = "topLeftLatitude 값이 없습니다. ") Double topLeftLatitude,
        @NotNull(message = "bottomRightLongitude 값이 없습니다.") Double bottomRightLongitude,
        @NotNull(message = "bottomRightLatitude 값이 없습니다.") Double bottomRightLatitude
    ) {

        public PlacesCommand.Coordinate toCommand() {
            return new PlacesCommand.Coordinate(
                topLeftLongitude, topLeftLatitude, bottomRightLongitude, bottomRightLatitude,
                longitude, latitude
            );
        }
    }

    public record Filter(
        String regions,
        String categories,
        String influencers
    ) {

        public Filter {
            if (regions == null) {
                regions = "";
            }
            if (categories == null) {
                categories = "";
            }
            if (influencers == null) {
                influencers = "";
            }
        }

        public PlacesCommand.FilterParams toCommand() {

            List<RegionParam> regionParamList = Arrays.stream(regions.split(","))
                .filter(StringUtils::isNotEmpty)
                .map((region -> {
                    var parts = regions.split("-");
                    return new RegionParam(
                        parts[0],
                        "전체".equals(parts[1]) ? null : parts[1]
                    );
                })).toList();

            List<Category> categoryList = Arrays.stream(categories.split(","))
                .map(Category::of)
                .filter(Objects::nonNull)
                .toList();
            List<String> influencerList = Arrays.stream(influencers.split(","))
                .filter(StringUtils::isNotEmpty)
                .toList();

            return new FilterParams(
                regionParamList,
                categoryList,
                influencerList
            );
        }
    }
}
