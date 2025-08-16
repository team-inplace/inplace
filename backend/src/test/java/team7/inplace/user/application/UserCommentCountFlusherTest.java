package team7.inplace.user.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import team7.inplace.user.application.UserCommentCountFlusherTest.TestCacheConfig;
import user.util.UserCommentCountFlusher;
import user.UserWriteQueryDslRepository;
import user.jpa.UserJpaRepository;

@DataJpaTest
@Import({TestCacheConfig.class, UserCommentCountFlusher.class, UserWriteQueryDslRepository.class,
    ObjectMapper.class})
@EnableCaching
@ActiveProfiles("test")
@Sql("/sql/test-user.sql")
class UserCommentCountFlusherTest {

    @Autowired
    CacheManager cacheManager;

    @Autowired
    UserCommentCountFlusher userCommentCountFlusher;

    @Autowired
    UserJpaRepository userJpaRepository;

    @Test
    void flushReceivedCommentCounts() {
        // given
        Long userId1 = 1L;
        Long userId2 = 3L;
        Long expected1 = 1123L;
        Long expected2 = 12L;
        Cache cache = cacheManager.getCache("receivedCommentCache");
        Objects.requireNonNull(cache).put(userId1, expected1);
        cache.put(userId2, expected2);

        //when
        userCommentCountFlusher.flushReceivedCommentCounts();

        //then
        Long actual1 = userJpaRepository.findById(userId1).get().getReceivedCommentCount();
        Long actual2 = userJpaRepository.findById(userId2).get().getReceivedCommentCount();
        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    @TestConfiguration
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
