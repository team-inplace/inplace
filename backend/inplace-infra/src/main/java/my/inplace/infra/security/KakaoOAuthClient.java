package my.inplace.infra.security;

import my.inplace.common.exception.InplaceException;
import my.inplace.common.exception.code.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;
import my.inplace.domain.security.OAuthSecurityClient;

@RequiredArgsConstructor
public class KakaoOAuthClient implements OAuthSecurityClient {

    private final String UNLINK_URL = "https://kapi.kakao.com/v1/user/unlink";
    private final RestTemplate restTemplate;

    public void unLink(String accessToken) {
        var header = new HttpHeaders();
        header.set("Authorization", "Bearer " + accessToken);
        header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        var request = restTemplate.exchange(RequestEntity
                .method(HttpMethod.POST, UNLINK_URL)
                .headers(header)
                .build(),
            String.class);

        if (request.getStatusCode().isError()) {
            throw InplaceException.of(UserErrorCode.FAIL_OAUTH_TOKEN_UNLINKE);
        }
    }
}
