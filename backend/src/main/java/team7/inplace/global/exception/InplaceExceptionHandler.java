package team7.inplace.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Profile("prod")
@RestControllerAdvice
@RequiredArgsConstructor
public class InplaceExceptionHandler {

    private final static HttpStatus SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR;
    private final static String SERVER_ERROR_MESSAGE = "서버 내부에서 오류가 발생하였습니다.";

    @ExceptionHandler(InplaceException.class)
    public ResponseEntity<ProblemDetail> handleInplaceException(
        HttpServletRequest request,
        InplaceException exception
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            SERVER_ERROR, // exception.getHttpStatus(),
            SERVER_ERROR_MESSAGE // exception.getMessage()
        );
        problemDetail.setTitle(exception.getErrorCode());
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return new ResponseEntity<>(problemDetail, exception.getHttpStatus());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAuthorizationDeniedException(
        HttpServletRequest request,
        AuthorizationDeniedException exception
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            SERVER_ERROR, // HttpStatus.FORBIDDEN,
            SERVER_ERROR_MESSAGE // exception.getMessage()
        );
        problemDetail.setTitle("E403");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return new ResponseEntity<>(problemDetail, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(
        HttpServletRequest request,
        MethodArgumentNotValidException exception
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            SERVER_ERROR, // HttpStatus.BAD_REQUEST,
            SERVER_ERROR_MESSAGE // exception.getMessage()
        );
        problemDetail.setTitle("E400");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleException(
        HttpServletRequest request,
        Exception exception
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            SERVER_ERROR, // HttpStatus.INTERNAL_SERVER_ERROR,
            SERVER_ERROR_MESSAGE // exception.getMessage()
        );
        problemDetail.setTitle("E999");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return new ResponseEntity<>(problemDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
