package team7.inplace.user.persistence;

import java.util.Map;

public interface UserWriteRepository {

    void updateBatchReceivedCommentCount(Map<Long, Long> counts);
}
