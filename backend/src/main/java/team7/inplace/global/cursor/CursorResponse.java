package team7.inplace.global.cursor;

public record CursorResponse(
    boolean hasNext,
    Long nextCursorId
) {

}
