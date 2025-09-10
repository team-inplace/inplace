package my.inplace.application.place.query.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import my.inplace.domain.place.client.GooglePlaceClientResponse;

public class GooglePlaceResult {

    public record Place(
        String id,
        double rating,
        Optional<GooglePlaceResult.RegularOpeningHours> regularOpeningHours,
        Optional<List<GooglePlaceResult.Review>> reviews,
        Optional<GooglePlaceResult.AccessibilityOptions> accessibilityOptions,
        Optional<GooglePlaceResult.ParkingOptions> parkingOptions,
        Optional<GooglePlaceResult.PaymentOptions> paymentOptions,
        String googleMapsUri
    ) {

        public static GooglePlaceResult.Place from(GooglePlaceClientResponse.Place googleClientResponse) {
            return new GooglePlaceResult.Place(
                googleClientResponse.id(),
                googleClientResponse.rating(),
                googleClientResponse.regularOpeningHours().map(GooglePlaceResult.RegularOpeningHours::from),
                googleClientResponse.reviews().map(GooglePlaceResult.Review::from),
                googleClientResponse.accessibilityOptions().map(GooglePlaceResult.AccessibilityOptions::from),
                googleClientResponse.parkingOptions().map(GooglePlaceResult.ParkingOptions::from),
                googleClientResponse.paymentOptions().map(GooglePlaceResult.PaymentOptions::from),
                googleClientResponse.googleMapsUri()
            );
        }
    }

    public record RegularOpeningHours(
        List<String> weekdayDescriptions
    ) {

        public static GooglePlaceResult.RegularOpeningHours from(
            GooglePlaceClientResponse.RegularOpeningHours regularOpeningHours) {
            return new GooglePlaceResult.RegularOpeningHours(
                regularOpeningHours.weekdayDescriptions()
            );
        }
    }

    public record Review(
        Integer rating,
        Optional<GooglePlaceResult.TextContent> text,
        GooglePlaceResult.AuthorAttribution authorAttribution,
        LocalDateTime publishTime
    ) {

        public static List<GooglePlaceResult.Review> from(List<GooglePlaceClientResponse.Review> reviews) {
            return reviews.stream()
                .map(review -> new GooglePlaceResult.Review(
                    review.rating(),
                    review.text().map(GooglePlaceResult.TextContent::from),
                    GooglePlaceResult.AuthorAttribution.from(review.authorAttribution()),
                    review.publishTime()
                ))
                .toList();
        }
    }

    public record TextContent(
        Optional<String> text
    ) {

        public static GooglePlaceResult.TextContent from(GooglePlaceClientResponse.TextContent textContent) {
            return new GooglePlaceResult.TextContent(
                textContent.text()
            );
        }
    }

    public record AuthorAttribution(
        String displayName
    ) {

        public static GooglePlaceResult.AuthorAttribution from(GooglePlaceClientResponse.AuthorAttribution authorAttribution) {
            return new AuthorAttribution(authorAttribution.displayName());
        }
    }

    public record AccessibilityOptions(
        Optional<Boolean> wheelchairAccessibleParking,
        Optional<Boolean> wheelchairAccessibleEntrance,
        Optional<Boolean> wheelchairAccessibleRestroom,
        Optional<Boolean> wheelchairAccessibleSeating
    ) {

        public static GooglePlaceResult.AccessibilityOptions from(
            GooglePlaceClientResponse.AccessibilityOptions accessibilityOptions) {
            return new GooglePlaceResult.AccessibilityOptions(
                accessibilityOptions.wheelchairAccessibleParking(),
                accessibilityOptions.wheelchairAccessibleEntrance(),
                accessibilityOptions.wheelchairAccessibleRestroom(),
                accessibilityOptions.wheelchairAccessibleSeating()
            );
        }
    }

    public record ParkingOptions(
        Optional<Boolean> freeParkingLot,
        Optional<Boolean> paidParkingLot,
        Optional<Boolean> freeStreetParking,
        Optional<Boolean> paidStreetParking,
        Optional<Boolean> valetParking,
        Optional<Boolean> freeGarageParking,
        Optional<Boolean> paidGarageParking
    ) {

        public static GooglePlaceResult.ParkingOptions from(GooglePlaceClientResponse.ParkingOptions parkingOptions) {
            return new GooglePlaceResult.ParkingOptions(
                parkingOptions.freeParkingLot(),
                parkingOptions.paidParkingLot(),
                parkingOptions.freeStreetParking(),
                parkingOptions.paidStreetParking(),
                parkingOptions.valetParking(),
                parkingOptions.freeGarageParking(),
                parkingOptions.paidGarageParking()
            );
        }
    }

    public record PaymentOptions(
        Optional<Boolean> acceptsCreditCards,
        Optional<Boolean> acceptsCashOnly
    ) {

        public static GooglePlaceResult.PaymentOptions from(GooglePlaceClientResponse.PaymentOptions paymentOptions) {
            return new GooglePlaceResult.PaymentOptions(
                paymentOptions.acceptsCreditCards(),
                paymentOptions.acceptsCashOnly()
            );
        }
    }
}
