package team7.inplace.user.util;

import com.github.benmanes.caffeine.cache.Cache;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import team7.inplace.user.persistence.UserWriteRepository;

@Component
@RequiredArgsConstructor
public class UserCommentCountFlusher {

    private final CacheManager cacheManager;
    private final UserWriteRepository userWriteRepository;

    @Transactional
    public Set<Long> flushReceivedCommentCounts() {
        CaffeineCache caffeineCache = (CaffeineCache) cacheManager.getCache("receivedCommentCache");
        if (Objects.isNull(caffeineCache)) {
            return null;
        }

        Cache<Object, Object> cache = caffeineCache.getNativeCache();

        Map<Long, Long> counts = cache.asMap().entrySet().stream()
            .filter(e -> e.getKey() instanceof Long && e.getValue() instanceof Long)
            .collect(Collectors.toMap(
                    e -> (Long) e.getKey(),
                    e -> (Long) e.getValue()
                )
            );
        Set<Long> set = counts.keySet();

        userWriteRepository.updateBatchReceivedCommentCount(counts);

        cache.invalidateAll();

        return set;
    }
}
