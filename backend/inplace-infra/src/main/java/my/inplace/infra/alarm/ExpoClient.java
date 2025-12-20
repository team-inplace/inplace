package my.inplace.infra.alarm;

import lombok.extern.slf4j.Slf4j;
import my.inplace.infra.alarm.dto.ExpoRequest;
import my.inplace.infra.annotation.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Client
public class ExpoClient {
    
    @Value(value = "${expo.url}")
    private String EXPO_URL;
    private final RestTemplate restTemplate = new RestTemplate();
    
    public void sendMessageByToken(String title, String body, String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<ExpoRequest> entity = new HttpEntity<>(ExpoRequest.of(token, title, body), httpHeaders);
        
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(EXPO_URL, entity, String.class);
            log.info("Expo 메세지 전송 성공 : {}", response.getBody());
        } catch (Exception e) {
            log.error("Expo 메세지 전송 실패 : ", e);
            throw new RuntimeException();
        }
    }
}
