package team7.inplace.global.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum AuthorizationErrorCode implements ErrorCode {
    NOT_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "A000", "인증이 실패했습니다."),
    TOKEN_IS_EMPTY(HttpStatus.BAD_REQUEST, "A001", "요청에 토큰이 포함되어있지 않습니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "A002", "유효하지 않은 토큰입니다."),
    TOKEN_IS_EXPIRED(HttpStatus.BAD_REQUEST, "A003", "만료된 토큰입니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }

    @Override
    public String code() {
        return errorCode;
    }

    @Override
    public String message() {
        return message;
    }
}
