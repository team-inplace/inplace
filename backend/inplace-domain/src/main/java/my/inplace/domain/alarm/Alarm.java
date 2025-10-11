package my.inplace.domain.alarm;

import jakarta.persistence.*;
import my.inplace.domain.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "Alarms")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alarm extends BaseEntity {

    private Long userId;
    private Long postId;
    @Embedded
    private AlarmComment alarmComment;
    private String content;
    private boolean checked;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    public Alarm(Long userId, Long postId, AlarmComment alarmComment, String content, AlarmType alarmType) {
        this.userId = userId;
        this.postId = postId;
        this.alarmComment = alarmComment;
        this.content = content;
        this.alarmType = alarmType;
    }

    public void checked() {
        this.checked = true;
    }
}
