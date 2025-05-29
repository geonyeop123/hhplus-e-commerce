package kr.hhplus.be.infra.userCoupon;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;


@Component
@RequiredArgsConstructor
public class RedisUserCouponRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String KEY_PREFIX = "coupon:";
    private static final String CALL_KEY_SUFFIX = ":callIssue";
    private static final String ISSUED_KEY_SUFFIX = ":issued";

    private static final String ISSUE_LUA = """
        local cur = redis.call('ZSCORE', KEYS[1], ARGV[1])
        if (not cur) or (tonumber(cur) < 0) then
          return 0
        end
        redis.call('ZREM', KEYS[1], ARGV[1])
        redis.call('SADD', KEYS[2], ARGV[1])
        return 1
    """;

    private static final RedisScript<Long> ISSUE_SCRIPT  =
            new DefaultRedisScript<>(ISSUE_LUA, Long.class);

    public List<Long> findIssueTarget(Long couponId, int quantity){

        if(quantity <= 0){
            return List.of();
        }

        String key = KEY_PREFIX + couponId + CALL_KEY_SUFFIX;
        Set<ZSetOperations.TypedTuple<Object>> typedTuples = redisTemplate.opsForZSet().rangeWithScores(key, 0, quantity - 1);

        if(typedTuples == null || typedTuples.isEmpty()){
            return List.of();
        }

        return typedTuples.stream().map(tuple -> {
            String userIdKey = String.valueOf(tuple.getValue());
            return Long.parseLong(userIdKey.split(":")[1]);
        }).toList();
    }

    public void issueChecked(Long userId, Long couponId) {
        String callKey   = KEY_PREFIX + couponId + CALL_KEY_SUFFIX;
        String issuedKey = KEY_PREFIX + couponId + ISSUED_KEY_SUFFIX;
        String member    = "userId:" + userId;

        redisTemplate.execute(
                ISSUE_SCRIPT,
                List.of(callKey, issuedKey),
                member
        );
    }
}
