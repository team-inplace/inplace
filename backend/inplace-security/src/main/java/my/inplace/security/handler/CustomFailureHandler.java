package my.inplace.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import my.inplace.common.exception.InplaceException;
import my.inplace.common.exception.code.AuthorizationErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j
public class CustomFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Value("${spring.redirect.front-end-url}")
    private String frontEndUrl;

    public CustomFailureHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException exception
    ) throws IOException {
        log.error("OAUTH2 FAILURE >>> {}", exception.getClass().getName(), exception);
        log.error("CAUSE >>> {}", exception.getCause(), exception.getCause());
        String accept = request.getHeader("Accept");
        if (StringUtils.hasText(accept) && accept.contains("text/html")) {
            response.sendRedirect(frontEndUrl);
        }
        setErrorResponse(response, InplaceException.of(AuthorizationErrorCode.NOT_AUTHENTICATION));
    }

    private void setErrorResponse(
        HttpServletResponse response,
        InplaceException inplaceException
    ) throws IOException {
        response.setStatus(inplaceException.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            inplaceException.getHttpStatus(), inplaceException.getMessage());
        response.getWriter().write(objectMapper.writeValueAsString(problemDetail));
    }
}
