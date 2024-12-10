package team7.inplace.global.kakao.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao.api")
public record KakaoApiProperties(
        String key,
        String jsKey,
        String sendMessageToMeUrl
) {

    public String getAuthorization() {
        return "KakaoAK " + key;
    }
}
