package team7.inplace.place.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import team7.inplace.global.annotation.Client;
import team7.inplace.global.properties.GoogleApiProperties;
import team7.inplace.place.client.GooglePlaceClientResponse.Place;

@Slf4j
@Client("Google Place Client")
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

        try {
            log.info("Google Place Detail 요청 시작. URL: {}, Place ID: {}", url, placeId);
            var response = restTemplate.exchange(url, HttpMethod.GET, entity,
                GooglePlaceClientResponse.Place.class);
            log.info("Google Place Detail 요청 성공. 상태 코드: {}", response.getStatusCode());
            return response.getBody();
        } catch (ResourceAccessException e) {
            // 네트워크 연결 문제, DNS 오류, 타임아웃 등 I/OException 계열 오류 발생 시
            log.error("Google Place Detail 요청 중 네트워크 또는 I/O 오류 발생: " + e.getMessage(), e);
            // 필요에 따라 예외를 다시 던지거나, 특정 에러 응답을 반환할 수 있습니다.
            throw new RuntimeException("Google Place API 네트워크 오류 발생: " + e.getMessage(), e);
        } catch (HttpClientErrorException e) {
            // 4xx 클라이언트 오류 (예: 400 Bad Request, 401 Unauthorized, 404 Not Found)
            log.error("Google Place Detail 요청 중 클라이언트 오류 발생 (HTTP {}): {} - 응답 본문: {}",
                e.getStatusCode(), e.getMessage(), e.getResponseBodyAsString(), e);
            throw new RuntimeException("Google Place API 클라이언트 오류 발생: " + e.getMessage(), e);
        } catch (RestClientException e) {
            // 그 외 RestTemplate 관련 일반적인 오류
            log.error("Google Place Detail 요청 중 알 수 없는 RestClientException 발생: " + e.getMessage(),
                e);
            throw new RuntimeException("Google Place API 요청 중 알 수 없는 오류 발생: " + e.getMessage(), e);
        } catch (Exception e) {
            // 예상치 못한 다른 모든 예외
            log.error("Google Place Detail 요청 중 예상치 못한 오류 발생: " + e.getMessage(), e);
            throw new RuntimeException("Google Place API 요청 중 예상치 못한 오류 발생: " + e.getMessage(), e);
        }

    }
}
