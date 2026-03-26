package my.inplace.domain.alarm;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlarmOutBox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long receiverId;
    private String title;
    private String content;
    private Long alarmId;
    private Long postId;
    private Long commentId;
    private AlarmType alarmType;
    private AlarmStatus alarmStatus;

    public AlarmOutBox(Long receiverId, String title, String content, Long alarmId, Long postId, Long commentId, AlarmType alarmType) {
        this.receiverId = receiverId;
        this.title = title;
        this.content = content;
        this.alarmId = alarmId;
        this.postId = postId;
        this.commentId = commentId;
        this.alarmType = alarmType;
        this.alarmStatus = AlarmStatus.READY;
    }
    
    public void ready() {
        this.alarmStatus = AlarmStatus.READY;
    }
    
    public void published() {
        this.alarmStatus = AlarmStatus.PUBLISHED;
    }
    
    public void pending() {
        this.alarmStatus = AlarmStatus.PENDING;
    }
}
