package my.inplace.infra.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fcm") // TODO - springboot web 의존성
public record FcmProperties(
        String serviceAccountFile,
        String projectId
) {

}
