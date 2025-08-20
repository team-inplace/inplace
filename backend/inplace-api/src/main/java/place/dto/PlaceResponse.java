package place.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import place.client.GooglePlaceClientResponse;
import place.client.GooglePlaceClientResponse.AccessibilityOptions;
import place.client.GooglePlaceClientResponse.ParkingOptions;
import place.client.GooglePlaceClientResponse.PaymentOptions;
import place.client.GooglePlaceClientResponse.RegularOpeningHours;
import place.query.PlaceQueryResult;
import place.query.dto.PlaceResult;
import review.query.ReviewQueryResult;
import video.query.VideoQueryResult;

public class PlaceResponse {

    public record Simple(
        Long placeId,
        String placeName,
        Address address,
        String category,
        String menuImgUrl,
        String longitude,
        String latitude,
        Long likeCount,
        Boolean likes,
        List<PlaceResponse.Video> videos
    ) {

        public static List<Simple> from(List<PlaceInfo.Simple> placeInfos) {
            return placeInfos.stream()
                .map(Simple::from)
                .toList();
        }

        public static Simple from(PlaceInfo.Simple placeInfo) {
            return new Simple(
                placeInfo.place().placeId(),
                placeInfo.place().placeName(),
                new Address(
                    placeInfo.place().address1(),
                    placeInfo.place().address2(),
                    placeInfo.place().address3()
                ),
                placeInfo.place().category(),
                "",
                placeInfo.place().longitude().toString(),
                placeInfo.place().latitude().toString(),
                placeInfo.place().likeCount(),
                placeInfo.place().isLiked(),
                placeInfo.video().stream()
                    .map(PlaceResponse.Video::from)
                    .collect(Collectors.toList())
            );
        }
    }

    public record Detail(
        Long placeId,
        String placeName,
        PlaceResponse.Address address,
        String category,
        Double longitude,
        Double latitude,
        PlaceResponse.Facility facility,
        List<PlaceResponse.Video> videos,
        List<PlaceResponse.SurroundVideo> surroundVideos,
        List<PlaceResponse.GoogleReview> googleReviews,
        Double rating,
        String kakaoPlaceUrl,
        String googlePlaceUrl,
        String naverPlaceUrl,
        List<String> openingHours,
        ReviewLike reviewLikes,
        Long likeCount,
        Boolean likes
    ) {

        public static PlaceResponse.Detail from(PlaceInfo.Detail place) {
            if (place.googlePlace() == null) {
                return createDetailWithoutGooglePlace(place);
            }
            return createDetailWithGooglePlace(place);
        }

        private static PlaceResponse.Detail createDetailWithoutGooglePlace(
            PlaceInfo.Detail place
        ) {
            return new PlaceResponse.Detail(
                place.place().placeId(),
                place.place().placeName(),
                new PlaceResponse.Address(
                    place.place().address1(),
                    place.place().address2(),
                    place.place().address3()
                ),
                place.place().category(),
                place.place().longitude(),
                place.place().latitude(),
                null,
                place.videos().stream()
                    .map(PlaceResponse.Video::from)
                    .toList(),
                place.surroundVideos().stream()
                    .map(PlaceResponse.SurroundVideo::from)
                    .toList(),
                List.of(),
                null,
                "http://place.map.kakao.com/" + place.place().kakaoPlaceId(),
                null,
                "https://map.naver.com/p/search/"
                    + convertParamsForNaverSearch(
                    place.place().address1(),
                    place.place().address2(),
                    place.place().address3(),
                    place.place().placeName()
                ),
                List.of(),
                ReviewLike.from(place.reviewLikeRate()),
                place.place().likeCount(),
                place.place().isLiked()
            );
        }

        private static PlaceResponse.Detail createDetailWithGooglePlace(PlaceInfo.Detail place) {
            return new PlaceResponse.Detail(
                place.place().placeId(),
                place.place().placeName(),
                new PlaceResponse.Address(
                    place.place().address1(),
                    place.place().address2(),
                    place.place().address3()
                ),
                place.place().category(),
                place.place().longitude(),
                place.place().latitude(),
                PlaceResponse.Facility.of(
                    place.googlePlace().accessibilityOptions().orElse(null),
                    place.googlePlace().parkingOptions().orElse(null),
                    place.googlePlace().paymentOptions().orElse(null)
                ),
                place.videos().stream()
                    .map(PlaceResponse.Video::from)
                    .toList(),
                place.surroundVideos().stream()
                    .map(PlaceResponse.SurroundVideo::from)
                    .toList(),
                place.googlePlace().reviews().orElse(List.of())
                    .stream()
                    .map(PlaceResponse.GoogleReview::from)
                    .toList(),
                place.googlePlace().rating(),
                "http://place.map.kakao.com/" + place.place().kakaoPlaceId(),
                place.googlePlace().googleMapsUri(),
                "https://map.naver.com/p/search/"
                    + convertParamsForNaverSearch(
                    place.place().address1(),
                    place.place().address2(),
                    place.place().address3(),
                    place.place().placeName()
                ),
                place.googlePlace().regularOpeningHours()
                    .map(RegularOpeningHours::weekdayDescriptions)
                    .orElse(List.of()),
                ReviewLike.from(place.reviewLikeRate()),
                place.place().likeCount(),
                place.place().isLiked()
            );
        }

        private static String convertParamsForNaverSearch(String... params) {
            return String.join("%20", params);
        }
    }

    public record Address(
        String address1,
        String address2,
        String address3
    ) {

    }

    @JsonInclude(Include.NON_NULL)
    public record Facility(
        Boolean wheelchairAccessibleSeating,
        Boolean freeParkingLot,
        Boolean paidParkingLot,
        Boolean acceptsCreditCards,
        Boolean acceptsCashOnly
    ) {

        public static PlaceResponse.Facility of(
            GooglePlaceClientResponse.AccessibilityOptions accessibilityOptions,
            GooglePlaceClientResponse.ParkingOptions parkingOptions,
            GooglePlaceClientResponse.PaymentOptions paymentOptions
        ) {
            return new PlaceResponse.Facility(
                Optional.ofNullable(accessibilityOptions)
                    .flatMap(AccessibilityOptions::wheelchairAccessibleSeating)
                    .orElse(null),
                Optional.ofNullable(parkingOptions)
                    .flatMap(ParkingOptions::freeParkingLot)
                    .orElse(null),
                Optional.ofNullable(parkingOptions)
                    .flatMap(ParkingOptions::paidParkingLot)
                    .orElse(null),
                Optional.ofNullable(paymentOptions)
                    .flatMap(PaymentOptions::acceptsCreditCards)
                    .orElse(null),
                Optional.ofNullable(paymentOptions)
                    .flatMap(PaymentOptions::acceptsCashOnly)
                    .orElse(null)
            );
        }
    }

    public record GoogleReview(
        Boolean like,
        String text,
        String name,
        String publishTime
    ) {

        public static PlaceResponse.GoogleReview from(GooglePlaceClientResponse.Review review) {
            return new PlaceResponse.GoogleReview(
                review.rating() >= 4,
                review.text().isEmpty() ? "" : review.text().get().text().orElse(""),
                review.authorAttribution().displayName(),
                review.publishTime().toString()
            );
        }
    }

    public record Review(
        Long reviewId,
        boolean likes,
        String comment,
        String userNickname,
        LocalDate createdDate,
        boolean mine
    ) {

    }

    public record ReviewLike(
        Long like,
        Long dislike
    ) {

        public static ReviewLike from(ReviewQueryResult.LikeRate placeLike) {
            return new ReviewLike(placeLike.likes(), placeLike.dislikes());
        }
    }

    public record Video(
        String videoUrl,
        String influencerName
    ) {

        public static PlaceResponse.Video from(VideoQueryResult.SimpleVideo video) {
            return new PlaceResponse.Video(video.videoUrl(), video.influencerName());
        }
    }

    public record SurroundVideo(
        Long videoId,
        String influencerName,
        String videoUrl,
        PlaceResponse.PlaceDetail place
    ) {

        public static SurroundVideo from(VideoQueryResult.DetailedVideo videoInfo) {
            var place = new PlaceResponse.PlaceDetail(
                videoInfo.placeId(),
                videoInfo.placeName(),
                new PlaceResponse.Address(
                    videoInfo.address1(),
                    videoInfo.address2(),
                    videoInfo.address3()
                )
            );
            return new SurroundVideo(
                videoInfo.videoId(),
                videoInfo.influencerName(),
                videoInfo.videoUrl(),
                place
            );
        }
    }

    public record PlaceDetail(
        Long placeId,
        String placeName,
        PlaceResponse.Address address
    ) {

    }


    public record Marker(
        Long placeId,
        String type,
        Double longitude,
        Double latitude
    ) {

        public static List<Marker> from(List<PlaceQueryResult.Marker> markers) {
            return markers.stream()
                .map(Marker::from)
                .toList();
        }

        private static Marker from(PlaceQueryResult.Marker marker) {
            return new Marker(
                marker.placeId(),
                marker.parentsCategory(),
                marker.longitude(),
                marker.latitude()
            );
        }
    }

    public record MarkerDetail(
        Long placeId,
        String placeName,
        String category,
        Address address,
        String menuImgUrl,
        List<Video> videos
    ) {

        public static MarkerDetail from(PlaceInfo.Marker marker) {
            return new MarkerDetail(
                marker.place().placeId(),
                marker.place().placeName(),
                marker.place().category(),
                new Address(
                    marker.place().address1(),
                    marker.place().address2(),
                    marker.place().address3()
                ),
                "",
                marker.videos().stream()
                    .map(PlaceResponse.Video::from)
                    .toList()
            );
        }
    }

    public record Categories(
        List<Category> categories
    ) {

        public static Categories from(List<PlaceInfo.Category> categories) {
            Map<Long, Category> categoryMap = new HashMap<>();
            categories.stream()
                .filter(category -> category.parentId() == null)
                .forEach(category -> {
                    Category categoryResponse = Category.from(category);
                    categoryMap.put(category.id(), categoryResponse);
                });
            categories.stream()
                .filter(category -> category.parentId() != null)
                .forEach(category -> {
                    Category parentCategory = categoryMap.get(category.parentId());
                    if (parentCategory != null) {
                        parentCategory.subCategories().add(Category.from(category));
                    }
                });
            return new Categories(new ArrayList<>(categoryMap.values()));
        }
    }

    public record Category(
        Long id,
        String name,
        @JsonInclude(Include.NON_EMPTY) List<Category> subCategories
    ) {

        public static Category from(PlaceInfo.Category category) {
            return new Category(category.id(), category.name(), new ArrayList<>());
        }
    }

    public record Admin(
        Long placeId,
        String placeName,
        String category,
        String address,
        Double x,
        Double y,
        Long kakaoPlaceId,
        String googlePlaceId
    ) {

        public static Admin of(PlaceInfo.Simple simple) {
            return new PlaceResponse.Admin(
                simple.place().placeId(),
                simple.place().placeName(),
                simple.place().category(),
                simple.place().address1() + " " + simple.place().address2() + " " + simple.place()
                    .address3(),
                simple.place().longitude(),
                simple.place().latitude(),
                simple.place().kakaoPlaceId(),
                simple.place().googlePlaceId()
            );
        }
    }

    public record AdminCategory(
        Long id,
        String name,
        String engName,
        Long parentId
    ) {

        public static AdminCategory of(PlaceResult.Category category) {
            return new AdminCategory(
                category.id(),
                category.name(),
                category.engName(),
                category.parentId()
            );
        }
    }

}
