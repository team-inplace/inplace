package my.inplace.application.user.job;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import my.inplace.infra.user.util.UserCommentCountFlusher;

@Component
@RequiredArgsConstructor
public class UserGradeUpdateJob {

    private final UserCommentCountFlusher userCommentCountFlusher;
    private final UserGradeUpdater userGradeUpdater;

    @Scheduled(cron = "0 */5 * * * *")
    public void updateUserGrades() {
        Set<Long> userIds = userCommentCountFlusher.flushReceivedCommentCounts();
        userGradeUpdater.updateGradesByUserIds(userIds);
    }
}
