package team7.inplace.global.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "google.api")
public record GoogleApiProperties(
    String crawlingKey,
    String placeKey1,
    String placeKey2,
    String placeKey3
) {

    public String[] getGoogleKey() {
        return new String[]{crawlingKey, placeKey1, placeKey2, placeKey3};
    }
}
