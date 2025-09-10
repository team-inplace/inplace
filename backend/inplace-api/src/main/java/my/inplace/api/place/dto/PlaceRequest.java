package my.inplace.api.place.dto;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.inplace.application.place.command.dto.PlaceCommand;
import my.inplace.application.place.query.dto.PlaceParam;

public class PlaceRequest {

    public record Like(
        Long placeId,
        Boolean likes
    ) {

    }

    public record Upsert(
        Long videoId,
        String placeName,
        Long categoryId,
        String address,
        String x,
        String y,
        Long kakaoPlaceId,
        String googlePlaceId
    ) {

        public PlaceCommand.Create toCreateCommand() {
            return new PlaceCommand.Create(
                videoId, placeName, categoryId, address, x, y, googlePlaceId, kakaoPlaceId
            );
        }

        public PlaceCommand.Update toUpdateCommand(Long placeId) {
            return new PlaceCommand.Update(
                placeId, placeName, categoryId, address, x, y, googlePlaceId, kakaoPlaceId
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

        public PlaceParam.Coordinate toParam() {
            return new PlaceParam.Coordinate(
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

        public PlaceParam.Filter toParam() {

            List<PlaceParam.Region> regionParamList = Arrays.stream(regions.split(","))
                .filter(StringUtils::isNotEmpty)
                .map((region -> {
                    var parts = regions.split("-");
                    return new PlaceParam.Region(
                        parts[0],
                        "전체".equals(parts[1]) ? null : parts[1]
                    );
                })).toList();

            List<Long> categoryList = Arrays.stream(categories.split(","))
                .map(String::trim)
                .filter(StringUtils::isNotEmpty)
                .map(Long::valueOf)
                .toList();
            List<String> influencerList = Arrays.stream(influencers.split(","))
                .filter(StringUtils::isNotEmpty)
                .toList();

            return new PlaceParam.Filter(
                regionParamList,
                categoryList,
                influencerList
            );
        }
    }

    public record CategoryUpdate(
        String name,
        String engName,
        Long parentId
    ) {

        public PlaceCommand.CategoryUpdate toCommand(Long categoryId) {
            return new PlaceCommand.CategoryUpdate(
                categoryId,
                this.name,
                this.engName,
                this.parentId
            );
        }
    }

    @NoArgsConstructor
    @Getter @Setter
    public static final class CategoryCreate {
        private String name;
        private String engName;
        private Long parentId;

        public PlaceCommand.CategoryCreate toCommand() {
            return new PlaceCommand.CategoryCreate(
                this.name,
                this.engName,
                this.parentId
            );
        }
    }

}
