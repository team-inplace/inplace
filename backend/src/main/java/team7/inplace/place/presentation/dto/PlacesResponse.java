package team7.inplace.place.presentation.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import team7.inplace.place.application.dto.PlaceQueryInfo;
import team7.inplace.place.application.dto.PlaceQueryInfo.Marker;
import team7.inplace.place.domain.Category;
import team7.inplace.place.persistence.dto.PlaceQueryResult;
import team7.inplace.review.persistence.dto.ReviewQueryResult;
import team7.inplace.video.persistence.dto.VideoQueryResult.SimpleVideo;

public class PlacesResponse {

    public record Simple(
        Long placeId,
        String placeName,
        Address address,
        String category,
        String influencerName,
        String menuImgUrl,
        String longitude,
        String latitude,
        Boolean likes
    ) {

        public static List<Simple> from(List<PlaceQueryInfo.Simple> placeInfos) {
            return placeInfos.stream()
                .map(Simple::from)
                .toList();
        }

        public static Simple from(PlaceQueryInfo.Simple placeInfo) {
            return new Simple(
                placeInfo.place().placeId(),
                placeInfo.place().placeName(),
                Address.from(
                    placeInfo.place().address1(),
                    placeInfo.place().address2(),
                    placeInfo.place().address3()
                ),
                Category.valueOf(placeInfo.place().category()).getName(),
                placeInfo.video().stream().map(SimpleVideo::influencerName)
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.joining(", ")),
                placeInfo.place().menuImgUrl(),
                placeInfo.place().longitude().toString(),
                placeInfo.place().latitude().toString(),
                placeInfo.place().isLiked()
            );
        }
    }

    public record Detail(
        Long placeId,
        String placeName,
        Address address,
        String category,
        String influencerName,
        String longitude,
        String latitude,
        Boolean likes,
        JsonNode facilityInfo,
        PlacesResponse.MenuInfos menuInfos,
        PlacesResponse.OpenHour openHour,
        PlacesResponse.PlaceLikes placeLikes,
        List<String> videoUrl
    ) {

        public static Detail from(PlaceQueryInfo.Detail placeDetailInfo) {
            var placeBulk = placeDetailInfo.placeBulk();
            var videos = placeDetailInfo.videos();
            var placeReviewLikeRate = placeDetailInfo.reviewLikeRate();

            var menuInfos = PlacesResponse.MenuInfos.from(
                placeBulk.menuBordPhotos(),
                placeBulk.menus(),
                placeBulk.detailedPlace().menuUpdatedAt()
            );
            var openHour = PlacesResponse.OpenHour.from(placeBulk.openTimes(), placeBulk.offDays());
            var placeLikes = PlacesResponse.PlaceLikes.from(placeReviewLikeRate);

            var influencerNames = videos.stream().map(SimpleVideo::influencerName)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.joining(", "));
            var videoUrls = videos.stream()
                .map(SimpleVideo::videoUrl)
                .toList();

            return new Detail(
                placeBulk.detailedPlace().placeId(),
                placeBulk.detailedPlace().placeName(),
                Address.from(
                    placeBulk.detailedPlace().address1(),
                    placeBulk.detailedPlace().address2(),
                    placeBulk.detailedPlace().address3()
                ),
                Category.valueOf(placeBulk.detailedPlace().category()).getName(),
                influencerNames,
                placeBulk.detailedPlace().longitude().toString(),
                placeBulk.detailedPlace().latitude().toString(),
                placeBulk.detailedPlace().isLiked(),
                facilityTree(placeBulk.detailedPlace().facility()),
                menuInfos,
                openHour,
                placeLikes,
                videoUrls
            );
        }

        private static JsonNode facilityTree(String facility) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.readTree(facility);
            } catch (Exception e) {
                var noDataNode = objectMapper.createObjectNode();
                noDataNode.put("message", "");
                return noDataNode;
            }
        }
    }

    public record Address(
        String address1,
        String address2,
        String address3
    ) {

        public static Address from(String address1, String address2, String address3) {
            return new Address(address1, address2, address3);
        }
    }

    public record MenuInfos(
        List<String> menuImgUrls,
        List<Menu> menuList,
        String menuUpdatedAt
    ) {

        public static MenuInfos from(
            List<PlaceQueryResult.MenuBordPhoto> menuBoardPhotos,
            List<PlaceQueryResult.Menu> menus,
            LocalDateTime menuUpdatedAt
        ) {
            return new MenuInfos(
                menuBoardPhotos.stream().map(PlaceQueryResult.MenuBordPhoto::imgUrl).toList(),
                menus.stream().map(PlacesResponse.Menu::from).toList(),
                Optional.ofNullable(menuUpdatedAt)
                    .map(dateTime -> dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .orElse(null)
            );
        }
    }

    public record Menu(
        String menuName,
        String price,
        String menuImgUrl,
        Boolean recommended,
        String description
    ) {

        public static Menu from(PlaceQueryResult.Menu menu) {
            return new Menu(
                menu.name(),
                menu.price(),
                menu.imgUrl(),
                menu.recommend(),
                menu.description()
            );
        }
    }

    public record OpenHour(
        List<OpenTime> periodList,
        List<OffDay> offdayList
    ) {

        public static OpenHour from(
            List<PlaceQueryResult.OpenTime> openTimes,
            List<PlaceQueryResult.OffDay> closeTimes
        ) {
            return new OpenHour(
                openTimes.stream().map(OpenTime::from).toList(),
                closeTimes.stream().map(OffDay::from).toList()
            );
        }
    }

    public record OpenTime(
        String timeName,
        String timeSE,
        String dayOfWeek
    ) {

        public static OpenTime from(PlaceQueryResult.OpenTime openTime) {
            return new OpenTime(
                openTime.timeName(),
                openTime.timeSE(),
                openTime.dayOfWeek()
            );
        }
    }

    public record OffDay(
        String holidayName,
        String weekAndDay,
        String temporaryHolidays
    ) {

        public static OffDay from(PlaceQueryResult.OffDay offDay) {
            return new OffDay(
                offDay.holidayName(),
                offDay.weekAndDay(),
                offDay.temporaryHolidays()
            );
        }
    }

    public record PlaceLikes(
        String like,
        String dislike
    ) {

        public static PlaceLikes from(ReviewQueryResult.LikeRate likeRate) {
            return new PlaceLikes(likeRate.likes().toString(), likeRate.dislikes().toString());
        }
    }

    public record Location (
            Long placeId,
            Double longitude,
            Double latitude
    ) {
        public static List<Location> from(List<PlaceQueryResult.Location> locations) {
            return locations.stream()
                .map(PlacesResponse.Location::from)
                .toList();
        }

        private static Location from(PlaceQueryResult.Location location) {
            return new Location(
                location.placeId(),
                location.longitude(),
                location.latitude()
            );
        }
    }

    public record Marker(
        Long placeId,
        String placeName,
        String category,
        String influencerName,
        Address address,
        String menuImgUrl
    ) {

        public static Marker from(PlaceQueryInfo.Marker marker) {
            return new Marker(
                marker.place().placeId(),
                marker.place().placeName(),
                Category.valueOf(marker.place().category()).getName(),
                marker.influencerNames(),
                Address.from(
                    marker.place().address1(),
                    marker.place().address2(),
                    marker.place().address3()
                ),
                marker.place().menuImgUrl()
            );
        }
    }
}
