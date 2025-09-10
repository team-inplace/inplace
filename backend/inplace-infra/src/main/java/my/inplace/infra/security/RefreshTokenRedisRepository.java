package my.inplace.infra.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import my.inplace.domain.security.RefreshToken;
import my.inplace.infra.redis.RedisRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRedisRepository implements RedisRepository<RefreshToken> {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private final String REFRESH_TOKEN_PREFIX = "refresh:";
    private final TimeUnit timeoutUnit = TimeUnit.MILLISECONDS;

    @Override
    public void save(String key, RefreshToken value) {
        redisTemplate.opsForValue().set(REFRESH_TOKEN_PREFIX + key, value);
    }

    @Override
    public void save(String key, RefreshToken value, long timeout) {
        redisTemplate.opsForValue().set(REFRESH_TOKEN_PREFIX + key, value, timeout, timeoutUnit);
    }

    @Override
    public Optional<RefreshToken> get(String key) {
        var value = redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + key);

        return Optional.ofNullable(objectMapper.convertValue(value, RefreshToken.class));
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + key);
    }
}
