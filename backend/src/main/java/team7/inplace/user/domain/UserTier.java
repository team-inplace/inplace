package team7.inplace.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserTier {
    BRONZE("브론즈", "bronze", 0L, 0L, 0L),
    SILVER("실버", "silver", 5L, 10L, 10L),
    GOLD("골드", "gold", 15L, 30L, 50L);

//    PLATINUM("플래티넘", "platinum", 30L, 80L, 120L),
//    DIAMOND("다이아몬드", "diamond", 50L, 150L, 250L),
//    RUBY("루비", "ruby", 80L, 250L, 500L),
//    MASTER("마스터", "master", 120L, 400L, 1000L),
//    GRANDMASTER("그랜드마스터", "grandmaster", 180L, 600L, 3000L),
//    LEGEND("레전드", "legend", 250L, 1000L, 10000L);

    private final String grade;
    private final String engGrade;
    private final Long requiredPosts;
    private final Long requiredComments;
    private final Long requiredLikes;

    public static UserTier calculateTier(Long posts, Long comments, Long likes) {
        UserTier[] tiers = UserTier.values();
        int left = 0;
        int right = tiers.length -1;
        int resultIndex = 0;

        while (left <= right) {
            int mid = (left + right) / 2;
            UserTier midTier = tiers[mid];

            if (posts >= midTier.requiredPosts && comments >= midTier.requiredComments && likes >= midTier.requiredLikes) {
                resultIndex = mid;
                left = mid + 1;
            }
            else {
                right = mid - 1;
            }
        }

        return tiers[resultIndex];
    }
}
