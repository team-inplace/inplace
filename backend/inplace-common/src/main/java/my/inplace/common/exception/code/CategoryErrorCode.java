package my.inplace.common.exception.code;

import org.springframework.http.HttpStatus;

public enum CategoryErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "CA001", "카테고리를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    CategoryErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    @Override
    public HttpStatus httpStatus() {
        return status;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
