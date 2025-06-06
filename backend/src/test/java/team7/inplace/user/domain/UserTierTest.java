package team7.inplace.user.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

class UserTierTest {

    @Test
    @DisplayName("calculateTier - 브론즈인 경우")
    void bronzeTest() {
        //given
        long actualPosts = 10000;
        long actualComments = 1;
        long actualLikes = 10;
        UserTier expectedUserTier = UserTier.BRONZE;
        //when
        UserTier actualUserTier = UserTier.calculateTier(actualPosts, actualComments, actualLikes);
        //then
        assertThat(actualUserTier).isEqualTo(expectedUserTier);
    }

    @Test
    @DisplayName("calculateTier - 실버인 경우")
    void silverTest() {
        //given
        long actualPosts = 5;
        long actualComments = 10;
        long actualLikes = 50;
        UserTier expectedUserTier = UserTier.SILVER;
        //when
        UserTier actualUserTier = UserTier.calculateTier(actualPosts, actualComments, actualLikes);
        //then
        assertThat(actualUserTier).isEqualTo(expectedUserTier);
    }

    @Test
    @DisplayName("calculateTier - 골드인 경우")
    void goldTest() {
        //given
        long actualPosts = 15;
        long actualComments = 30;
        long actualLikes = 50;
        UserTier expectedUserTier = UserTier.GOLD;
        //when
        UserTier actualUserTier = UserTier.calculateTier(actualPosts, actualComments, actualLikes);
        //then
        assertThat(actualUserTier).isEqualTo(expectedUserTier);
    }
}
