package my.inplace.common.cursor;

import java.util.List;

public record CursorResult<T, C>(
    List<T> value,
    boolean hasNext,
    C nextCursorValue,
    Long nextCursorId
) {

}
