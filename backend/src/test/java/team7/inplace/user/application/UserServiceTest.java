package team7.inplace.user.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import team7.inplace.post.persistence.PostJpaRepository;
import team7.inplace.user.domain.Role;
import team7.inplace.user.domain.User;
import team7.inplace.user.domain.UserType;
import team7.inplace.user.persistence.UserBadgeJpaRepository;
import team7.inplace.user.persistence.UserJpaRepository;
import team7.inplace.user.persistence.UserReadRepository;
import team7.inplace.user.persistence.UserTierJpaRepository;

@SpringJUnitConfig(classes = {
    UserServiceTest.TestCacheConfig.class,
    UserService.class
})
@ActiveProfiles("test")
class UserServiceTest {

    @MockBean private UserJpaRepository userJpaRepository;
    @MockBean private UserReadRepository userReadRepository;
    @MockBean private UserBadgeJpaRepository userBadgeJpaRepository;
    @MockBean private UserTierJpaRepository userTierJpaRepository;
    @MockBean private PostJpaRepository postJpaRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CacheManager cacheManager;

    @Test
    @DisplayName("Write Back 전략 - receivedCommentCount")
    void receivedCommentCountCacheUpdateTest() {
        // given
        Long userId = 1L;
        Integer delta = 3;
        Long expected = 5L;
        User user = new User("username", "password", "nickname", "imgUrl", UserType.KAKAO, Role.USER);
        user.updateReceivedCommentCount(2L);
        given(userJpaRepository.findById(userId)).willReturn(Optional.of(user));
        // when
        userService.addToReceivedCommentByUserId(userId, delta);
        Cache.ValueWrapper wrapper = cacheManager.getCache("receivedCommentCache").get(userId);
        assertNotNull(wrapper);
        assertThat(wrapper.get()).isEqualTo(expected);

        User updatedUser = userJpaRepository.findById(userId).orElseThrow();
        assertThat(updatedUser.getReceivedCommentCount()).isNotEqualTo(expected);
    }

    @Test
    @DisplayName("Write Back 전략 - receivedLikeCount")
    void receivedLikeCountUpdateTest() {
        // given
        Long userId = 2L;
        Integer delta = 3;
        Long expected = 110004L;
        User user = new User("username", "password", "nickname", "imgUrl", UserType.KAKAO, Role.USER);
        user.updateReceivedLikeCount(110001L);
        given(userJpaRepository.findById(userId)).willReturn(Optional.of(user));
        // when
        userService.addToReceivedLikeByUserId(userId, delta);

        Cache.ValueWrapper wrapper = cacheManager.getCache("receivedLikeCache").get(userId);
        assertNotNull(wrapper);
        assertThat(wrapper.get()).isEqualTo(expected);

        User updatedUser = userJpaRepository.findById(userId).orElseThrow();
        assertThat(updatedUser.getReceivedCommentCount()).isNotEqualTo(expected);
    }

    @TestConfiguration
    @EnableCaching
    public static class TestCacheConfig {

        @Bean
        CacheManager cacheManager() {
            CaffeineCacheManager cacheManager = new CaffeineCacheManager();
            cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1000));
            cacheManager.setCacheNames(List.of(
                "receivedCommentCache",
                "receivedLikeCache"));

            return cacheManager;
        }
    }
}
