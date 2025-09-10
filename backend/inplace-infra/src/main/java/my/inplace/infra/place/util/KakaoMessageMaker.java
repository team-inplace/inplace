package my.inplace.infra.place.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoMessageMaker {

    public static final String TEMPLATE_OBJECT = "template_object";
    private final ObjectMapper objectMapper;
    @Value("${spring.redirect.front-end-url}")
    private String frontEndUrl;

//    public MultiValueMap<String, String> createLocationTemplate(
//        PlaceMessageCommand placeMessageCommand
//    ) {
//        try {
//            LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//            body.add(TEMPLATE_OBJECT, objectMapper.writeValueAsString(
//                LocationTemplate.of(frontEndUrl, placeMessageCommand)));
//            return body;
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public MultiValueMap<String, String> createFeedTemplate(
//        PlaceMessageCommand placeMessageCommand, String uuid) {
//        try {
//            LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//            body.add(TEMPLATE_OBJECT, objectMapper.writeValueAsString(
//                FeedTemplate.of(frontEndUrl, placeMessageCommand, uuid)));
//            return body;
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
