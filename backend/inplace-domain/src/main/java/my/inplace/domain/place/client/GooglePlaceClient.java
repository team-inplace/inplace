package my.inplace.domain.place.client;

public interface GooglePlaceClient {

    String FIELD_HEADER = "X-Goog-FieldMask";
    String API_KEY_HEADER = "X-Goog-Api-Key";
    String PLACE_DETAIL_URL = "https://places.googleapis.com/v1/places/%s?languageCode=ko";
    String FIELD = "id,rating,reservable,regularOpeningHours,reviews,googleMapsUri,accessibilityOptions,parkingOptions,paymentOptions";

    default GooglePlaceClientResponse.Place requestForPlaceDetail(String placeId) {
        return null;
    }
}
