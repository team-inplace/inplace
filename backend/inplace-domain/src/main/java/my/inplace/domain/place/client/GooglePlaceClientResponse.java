package my.inplace.domain.place.client;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class GooglePlaceClientResponse {

    public record Place(
        String id,
        double rating,
        Optional<RegularOpeningHours> regularOpeningHours,
        Optional<List<Review>> reviews,
        Optional<AccessibilityOptions> accessibilityOptions,
        Optional<ParkingOptions> parkingOptions,
        Optional<PaymentOptions> paymentOptions,
        String googleMapsUri
    ) {

    }

    public record RegularOpeningHours(
        List<String> weekdayDescriptions
    ) {

    }

    public record Review(
        Integer rating,
        Optional<TextContent> text,
        AuthorAttribution authorAttribution,
        LocalDateTime publishTime
    ) {

    }

    public record TextContent(
        Optional<String> text
    ) {

    }

    public record AuthorAttribution(
        String displayName
    ) {

    }

    public record AccessibilityOptions(
        Optional<Boolean> wheelchairAccessibleParking,
        Optional<Boolean> wheelchairAccessibleEntrance,
        Optional<Boolean> wheelchairAccessibleRestroom,
        Optional<Boolean> wheelchairAccessibleSeating
    ) {

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

    }

    public record PaymentOptions(
        Optional<Boolean> acceptsCreditCards,
        Optional<Boolean> acceptsCashOnly
    ) {

    }
}
