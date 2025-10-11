package my.inplace.domain.alarm;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Embeddable
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode
public class AlarmComment {
    private Long commentId;
    private int pageNumber;
    private int offset;
    
    public AlarmComment(Long commentId, int pageNumber, int offset) {
        this.commentId = commentId;
        this.pageNumber = pageNumber;
        this.offset = offset;
    }
}
