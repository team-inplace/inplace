package my.inplace.application.alarm.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlarmEventListener {
    private final AlarmEventHandler alarmEventHandler;
    
    @EventListener
    public void processEventHandler(AlarmEvent alarmEvent) {
        switch (alarmEvent.alarmType()) {
            case MENTION -> alarmEventHandler.processMentionAlarm(alarmEvent);
            case REPORT -> processReportEventHandler(alarmEvent);
        }
    }
    
    private void processReportEventHandler(AlarmEvent alarmEvent) {
        if(alarmEvent.commentId() == null) {
            alarmEventHandler.processPostReportAlarm(alarmEvent);
            return;
        }
        
        alarmEventHandler.processCommentReportAlarm(alarmEvent);
    }
}
