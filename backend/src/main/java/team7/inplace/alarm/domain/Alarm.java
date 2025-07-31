package team7.inplace.alarm.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team7.inplace.global.baseEntity.BaseEntity;

@Entity
@Getter
@Table(name = "Alarms")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alarm extends BaseEntity {
    
    private Long userId;
    private Long postId;
    private Long commentId;
    private String content;
    private boolean checked;
    
    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;
    
    public Alarm(Long userId, Long postId, Long commentId, String content, AlarmType alarmType) {
        this.userId = userId;
        this.postId = postId;
        this.commentId = commentId;
        this.content = content;
        this.alarmType = alarmType;
    }
    
    public void checked() {
        this.checked = true;
    }
}
