package team7.inplace.user.application.dto;

import java.util.List;
import team7.inplace.user.domain.UserTier;

public class TierConditions {

    private final List<TierCondition> tierConditions;

    public TierConditions(List<TierCondition> tierConditions) {
        this.tierConditions = tierConditions;
    }

    public Long getCurrentTierId(Integer postCount, Long receivedCommentCount, Long receivedLikeCount) {
        int left = 0, right = tierConditions.size() - 1;
        int calculatedIdx = 0;
        while (left <= right) {
            int mid = (left + right) / 2;
            TierCondition tierCondition = tierConditions.get(mid);
            if (tierCondition.isSatisfied(postCount, receivedCommentCount, receivedLikeCount)) {
                calculatedIdx = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return tierConditions.get(calculatedIdx).id();
    }

    public static TierConditions of(List<UserTier> userTiers) {
        return new TierConditions(userTiers
            .stream()
            .map(TierCondition::from)
            .toList());
    }

    public record TierCondition(
        Long id,
        Integer requiredPosts,
        Long requiredComments,
        Long requiredLikes
    ) {

        public boolean isSatisfied(Integer postCount, Long receivedCommentCount, Long receivedLikeCount) {
            return postCount >= requiredPosts && receivedCommentCount >= requiredComments && receivedLikeCount >= requiredLikes;
        }

        private static TierCondition from(UserTier userTier) {
            return new TierCondition(userTier.getId(),
                userTier.getRequiredPosts(),
                userTier.getRequiredComments(),
                userTier.getRequiredLikes());
        }
    }
}
