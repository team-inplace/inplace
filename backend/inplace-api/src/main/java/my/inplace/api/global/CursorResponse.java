package my.inplace.api.global;

public record CursorResponse<C>(
    boolean hasNext,
    C nextCursorValue,
    Long nextCursorId
) {

}
