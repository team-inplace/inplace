package my.inplace.common.exception.code;

import org.springframework.http.HttpStatus;

public enum BannerErrorCode implements ErrorCode {
    NOT_FOUND("B001", "배너를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INFLUENCER_NOT_FOUND("B002", "인플루언서를 먼저 등록해야 합니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    BannerErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
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
