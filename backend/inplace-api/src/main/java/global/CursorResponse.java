package global;

public record CursorResponse(
    boolean hasNext,
    Long nextCursorId
) {

}
