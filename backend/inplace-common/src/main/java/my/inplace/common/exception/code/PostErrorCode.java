package my.inplace.common.exception.code;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum PostErrorCode implements ErrorCode {

    POST_TITLE_EMPTY(HttpStatus.BAD_REQUEST, "P001", "게시글 제목은 비어있을 수 없습니다."),
    POST_CONTENT_EMPTY(HttpStatus.BAD_REQUEST, "P002", "게시글 내용은 비어있을 수 없습니다."),
    POST_TITLE_LENGTH_EXCEEDED(HttpStatus.BAD_REQUEST, "P003", "게시글 제목은 30자를 초과할 수 없습니다."),
    POST_CONTENT_LENGTH_EXCEEDED(HttpStatus.BAD_REQUEST, "P004", "게시글 내용은 3000자를 초과할 수 없습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "P005", "게시글을 찾을 수 없습니다."),
    POST_PHOTOS_DUPLICATED(HttpStatus.BAD_REQUEST, "P006", "게시글 사진은 중복될 수 없습니다."),
    POST_PHOTOS_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "P007", "게시글 사진은 10개를 초과할 수 없습니다."),
    POST_CAN_NOT_BE_MODIFIED(HttpStatus.BAD_REQUEST, "P008", "게시글은 작성자만 수정/삭제 할 수 있습니다."),
    COMMENT_CONTENT_EMPTY(HttpStatus.BAD_REQUEST, "P009", "댓글 내용은 비어있을 수 없습니다."),
    COMMENT_LENGTH_EXCEEDED(HttpStatus.BAD_REQUEST, "P010", "댓글 내용은 1000자를 초과할 수 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "P011", "댓글을 찾을 수 없습니다."),
    COMMENT_CAN_NOT_BE_MODIFIED(HttpStatus.BAD_REQUEST, "P012", "댓글은 작성자만 수정/삭제 할 수 있습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public HttpStatus httpStatus() {
        return this.httpStatus;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String message() {
        return this.message;
    }
}
