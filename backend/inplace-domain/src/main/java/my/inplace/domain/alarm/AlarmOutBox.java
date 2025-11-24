package my.inplace.domain.alarm;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

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
    private AlarmStatus alarmStatus;
    
    public AlarmOutBox(Long receiverId, String title, String content) {
        this.receiverId = receiverId;
        this.title = title;
        this.content = content;
        this.alarmStatus = AlarmStatus.PENDING;
    }
    
    public void published() {
        this.alarmStatus = AlarmStatus.PUBLISHED;
    }
    
    public void pending() {
        this.alarmStatus = AlarmStatus.PENDING;
    }
}
