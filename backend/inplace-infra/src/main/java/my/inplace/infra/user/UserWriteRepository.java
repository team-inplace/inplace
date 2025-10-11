package my.inplace.infra.user;

import java.util.Map;

public interface UserWriteRepository {

    void updateBatchReceivedCommentCount(Map<Long, Long> counts);

    void updateBatchUserTiers(Map<Long, Long> tiers);
}
