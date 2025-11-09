package my.inplace.domain.alarm;

import jakarta.persistence.Entity;
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
    private UUID idempotencyKey;
    private String title;
    private String content;
    private String fcmToken;
    private String expoToken;
    private AlarmStatus alarmStatus;
    
    public AlarmOutBox(String title, String content, String fcmToken, String expoToken) {
        this.idempotencyKey = UUID.randomUUID();
        this.title = title;
        this.content = content;
        this.fcmToken = fcmToken;
        this.expoToken = expoToken;
        this.alarmStatus = AlarmStatus.PENDING;
    }
    
    public void published() {
        this.alarmStatus = AlarmStatus.PUBLISHED;
    }
    
    public void pending() {
        this.alarmStatus = AlarmStatus.PENDING;
    }
}
