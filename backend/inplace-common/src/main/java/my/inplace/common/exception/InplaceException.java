package my.inplace.common.exception;

import my.inplace.common.exception.code.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InplaceException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorMessage;

    private InplaceException(ErrorCode errorCode) {
        super(errorCode.message());
        this.httpStatus = errorCode.httpStatus();
        this.errorCode = errorCode.code();
        this.errorMessage = errorCode.message();
    }

    public static InplaceException of(ErrorCode errorCode) {
        return new InplaceException(errorCode);
    }
}
