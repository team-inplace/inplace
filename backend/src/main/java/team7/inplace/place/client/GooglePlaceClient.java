package team7.inplace.place.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import team7.inplace.global.properties.GoogleApiProperties;

@Component
@Slf4j
@RequiredArgsConstructor
public class GooglePlaceClient {

    private final String FIELD_HEADER = "X-Goog-FieldMask";
    private final String API_KEY_HEADER = "X-Goog-Api-Key";
    private final String PLACE_DETAIL_URL = "https://places.googleapis.com/v1/places/%s?languageCode=ko";
    private final String FIELD = "id,rating,reservable,regularOpeningHours,reviews,googleMapsUri,accessibilityOptions,parkingOptions,paymentOptions";
    private final WebClient webClient;
    private final GoogleApiProperties googleApiProperties;

    public Mono<GooglePlaceClientResponse.Place> requestForPlaceDetail(String placeId) {
        var url = String.format(PLACE_DETAIL_URL, placeId);

        return webClient.get()
            .uri(url)
            .header(FIELD_HEADER, FIELD)
            .header(API_KEY_HEADER, googleApiProperties.crawlingKey())
            .retrieve()
            .bodyToMono(GooglePlaceClientResponse.Place.class);
    }
}