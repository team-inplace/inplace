package team7.inplace.place.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import team7.inplace.place.application.dto.PlaceInfo;
import team7.inplace.place.client.GooglePlaceClientResponse;
import team7.inplace.place.client.GooglePlaceClientResponse.AccessibilityOptions;
import team7.inplace.place.client.GooglePlaceClientResponse.ParkingOptions;
import team7.inplace.place.client.GooglePlaceClientResponse.PaymentOptions;
import team7.inplace.place.client.GooglePlaceClientResponse.RegularOpeningHours;
import team7.inplace.place.persistence.dto.PlaceQueryResult;
import team7.inplace.review.persistence.dto.ReviewQueryResult;
import team7.inplace.video.persistence.dto.VideoQueryResult;

public class PlacesResponse {

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
        List<PlacesResponse.Video> videos
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
                placeInfo.place().category().getName(),
                "",
                placeInfo.place().longitude().toString(),
                placeInfo.place().latitude().toString(),
                placeInfo.place().likeCount(),
                placeInfo.place().isLiked(),
                placeInfo.video().stream()
                    .map(PlacesResponse.Video::from)
                    .collect(Collectors.toList())
            );
        }
    }

    public record Detail(
        Long placeId,
        String placeName,
        PlacesResponse.Address address,
        String category,
        Double longitude,
        Double latitude,
        PlacesResponse.Facility facility,
        List<PlacesResponse.Video> videos,
        List<PlacesResponse.GoogleReview> googleReviews,
        Double rating,
        String kakaoPlaceUrl,
        String googlePlaceUrl,
        String naverPlaceUrl,
        List<String> openingHours,
        ReviewLike reviewLikes,
        Long likeCount,
        Boolean likes
    ) {

        public static PlacesResponse.Detail from(PlaceInfo.Detail place) {
            if (place.googlePlace() == null) {
                return createDetailWithoutGooglePlace(place);
            }
            return createDetailWithGooglePlace(place);
        }

        private static PlacesResponse.Detail createDetailWithoutGooglePlace(
            PlaceInfo.Detail place
        ) {
            return new PlacesResponse.Detail(
                place.place().placeId(),
                place.place().placeName(),
                new PlacesResponse.Address(
                    place.place().address1(),
                    place.place().address2(),
                    place.place().address3()
                ),
                place.place().category().getName(),
                place.place().longitude(),
                place.place().latitude(),
                null,
                place.videos().stream()
                    .map(PlacesResponse.Video::from)
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

        private static PlacesResponse.Detail createDetailWithGooglePlace(PlaceInfo.Detail place) {
            return new PlacesResponse.Detail(
                place.place().placeId(),
                place.place().placeName(),
                new PlacesResponse.Address(
                    place.place().address1(),
                    place.place().address2(),
                    place.place().address3()
                ),
                place.place().category().getName(),
                place.place().longitude(),
                place.place().latitude(),
                PlacesResponse.Facility.of(
                    place.googlePlace().accessibilityOptions().orElse(null),
                    place.googlePlace().parkingOptions().orElse(null),
                    place.googlePlace().paymentOptions().orElse(null)
                ),
                place.videos().stream()
                    .map(PlacesResponse.Video::from)
                    .toList(),
                place.googlePlace().reviews().orElse(List.of())
                    .stream()
                    .map(PlacesResponse.GoogleReview::from)
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

        public static PlacesResponse.Facility of(
            GooglePlaceClientResponse.AccessibilityOptions accessibilityOptions,
            GooglePlaceClientResponse.ParkingOptions parkingOptions,
            GooglePlaceClientResponse.PaymentOptions paymentOptions
        ) {
            return new PlacesResponse.Facility(
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

        public static PlacesResponse.GoogleReview from(GooglePlaceClientResponse.Review review) {
            return new PlacesResponse.GoogleReview(
                review.rating() >= 3,
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

        public static PlacesResponse.Video from(VideoQueryResult.SimpleVideo video) {
            return new PlacesResponse.Video(video.videoUrl(), video.influencerName());
        }
    }

    public record Marker(
        Long placeId,
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
                marker.place().category().getName(),
                new Address(
                    marker.place().address1(),
                    marker.place().address2(),
                    marker.place().address3()
                ),
                "",
                marker.videos().stream()
                    .map(PlacesResponse.Video::from)
                    .toList()
            );
        }
    }

    public record Category(
        List<String> categories
    ) {

        public static Category from(List<PlaceInfo.Category> categories) {
            return new Category(categories.stream()
                .map(PlaceInfo.Category::name)
                .toList());
        }
    }
}
