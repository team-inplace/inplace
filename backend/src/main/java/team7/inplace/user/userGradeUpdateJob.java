package team7.inplace.user;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import team7.inplace.user.util.UserCommentCountFlusher;
import team7.inplace.user.util.UserGradeUpdater;

@Component
@RequiredArgsConstructor
public class userGradeUpdateJob {

    private final UserCommentCountFlusher userCommentCountFlusher;
    private final UserGradeUpdater userGradeUpdater;

    @Scheduled(cron = "0 */5 * * * *")
    public void updateUserGrades() {
        Set<Long> userIds = userCommentCountFlusher.flushReceivedCommentCounts();
        userGradeUpdater.updateGradesByUserIds(userIds);
    }
}
