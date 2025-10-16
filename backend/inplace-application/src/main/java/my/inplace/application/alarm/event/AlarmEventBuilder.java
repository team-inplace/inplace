package my.inplace.application.alarm.event;

import lombok.RequiredArgsConstructor;
import my.inplace.domain.alarm.AlarmType;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlarmEventBuilder {
    
    private Long postId;
    private Long commentId;
    private String sender;
    private String receiver;
    private AlarmType alarmType;
    
    public static AlarmEventBuilder create() {
        return new AlarmEventBuilder();
    }
    
    public AlarmEventBuilder postId(Long postId) {
        this.postId = postId;
        return this;
    }
    
    public AlarmEventBuilder commentId(Long commentId) {
        this.commentId = commentId;
        return this;
    }
    
    public AlarmEventBuilder sender(String sender) {
        this.sender = sender;
        return this;
    }
    
    public AlarmEventBuilder receiver(String receiver) {
        this.receiver = receiver;
        return this;
    }
    
    public AlarmEventBuilder type(AlarmType alarmType) {
        this.alarmType = alarmType;
        return this;
    }
    
    public AlarmEvent build() {
        return new AlarmEvent(
            this.postId,
            this.commentId,
            this.sender,
            this.receiver,
            this.alarmType
        );
    }
    
    public void publish(ApplicationEventPublisher eventPublisher) {
        eventPublisher.publishEvent(build());
    }
}