package my.inplace.api.global;

public record CursorResponse(
    boolean hasNext,
    Long nextCursorValue,
    Long nextCursorId
) {

}
