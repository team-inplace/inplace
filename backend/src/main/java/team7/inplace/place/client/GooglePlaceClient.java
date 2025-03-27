package team7.inplace.place.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import team7.inplace.global.properties.GoogleApiProperties;
import team7.inplace.place.client.GooglePlaceClientResponse.Place;

@Component
@RequiredArgsConstructor
public class GooglePlaceClient {

    private final String FIELD_HEADER = "X-Goog-FieldMask";
    private final String API_KEY_HEADER = "X-Goog-Api-Key";
    private final String PLACE_DETAIL_URL = "https://places.googleapis.com/v1/places/%s?languageCode=ko";
    private final String FIELD = "id,rating,reservable,regularOpeningHours,reviews,googleMapsUri,accessibilityOptions,parkingOptions,paymentOptions";
    private final RestTemplate restTemplate;
    private final GoogleApiProperties googleApiProperties;

    public Place requestForPlaceDetail(String placeId) {
        var url = String.format(PLACE_DETAIL_URL, placeId);
        HttpHeaders headers = new HttpHeaders();
        headers.set(FIELD_HEADER, FIELD);
        headers.set(API_KEY_HEADER, googleApiProperties.crawlingKey());

        HttpEntity<String> entity = new HttpEntity<>(headers);
        var response = restTemplate.exchange(url, HttpMethod.GET, entity,
            GooglePlaceClientResponse.Place.class);
        return response.getBody();

    }
}
