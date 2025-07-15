package team7.inplace.user.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import team7.inplace.user.domain.User;
import team7.inplace.user.persistence.UserJpaRepository;

@SpringBootTest
@ActiveProfiles("test")
@Sql("/sql/test-user.sql")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    @DisplayName("Write Back 전략 - receivedCommentCount")
    void testUpdateAndCachePut() {
        // given
        Long userId = 1L;
        Integer delta = 3;
        Long expected = 5L;

        // when
        Long updatedCount = userService.addToReceivedCommentByUserId(userId, delta);

        Cache.ValueWrapper wrapper = cacheManager.getCache("receivedCommentCache").get(userId);
        assertNotNull(wrapper);
        assertThat(wrapper.get()).isEqualTo(expected);

        User updatedUser = userJpaRepository.findById(userId).orElseThrow();
        assertThat(updatedUser.getReceivedCommentCount()).isNotEqualTo(expected);
    }
}
