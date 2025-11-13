package my.inplace.application.alarm.command;

import my.inplace.application.alarm.util.MentionMessageFactory;
import my.inplace.domain.alarm.Alarm;
import my.inplace.domain.alarm.AlarmComment;
import my.inplace.domain.alarm.AlarmOutBox;
import my.inplace.domain.alarm.AlarmType;
import my.inplace.infra.alarm.jpa.AlarmJpaRepository;
import lombok.RequiredArgsConstructor;
import my.inplace.infra.alarm.jpa.AlarmOutBoxRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmCommandService {
    
    private final AlarmJpaRepository alarmJpaRepository;
    private final AlarmOutBoxRepository alarmOutBoxRepository;
    
    private final MentionMessageFactory mentionMessageFactory;
    
    @Transactional
    public void mentionAlarm(Long postId, Long commentId, Long senderId, List<Long> receiverIds) {
        String title = mentionMessageFactory.createTitle();
        
        for (Long receiverId : receiverIds) {
            // 이거는 나중에 조회할 때 Facade 단에서 만들어서 주는게?
            Long index = postQueryService.getCommentIndexByPostIdAndCommentId(postId, commentId);
            
            // 토큰이 둘 다 Null 일때는 어캐할거?
            String fcmToken = userQueryService.getFcmTokenByUser(receiverId);
            String expoToken = userQueryService.getExpoTokenByUserId(receiverId);
            
            
            String content = mentionMessageFactory.createMessage(
                postQueryService.getPostTitleById(postId).getTitle(),
                // AuthorizationUtil 에서 Nickname 떼오기
                userQueryService.getUserInfo(senderId).nickname());
            
            // 비즈니스 데이터 저장
            alarmCommandService.saveAlarm(
                receiverId,
                postId,
                commentId,
                (int) (index / 10),
                (int) (index % 10),
                content,
                AlarmType.MENTION);
            
            // 이벤트 데이터 저장
            alarmCommandService.saveAlarmEvent(title, content, fcmToken, expoToken);
        }
    }

    @Transactional
    public void checkAlarm(Long id) {
        Alarm alarm = alarmJpaRepository.findById(id).orElseThrow();
        alarm.checked();
    }

    @Transactional
    public void saveAlarm(
        Long userId, Long postId, Long commentId, int pageNumber, int offset, String content, AlarmType alarmType) {
        Alarm alarm = new Alarm(
            userId,
            postId,
            new AlarmComment(commentId, pageNumber, offset),
            content,
            alarmType);

        alarmJpaRepository.save(alarm);
    }
    
    @Transactional
    public void saveAlarmEvent(
        String title, String content, String fcmToken, String expoToken
    ) {
        AlarmOutBox alarmEvent = new AlarmOutBox(title, content, fcmToken, expoToken);
        
        alarmOutBoxRepository.save(alarmEvent);
    }
}
