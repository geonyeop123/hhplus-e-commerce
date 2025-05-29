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

    private static final String CALL_LUA = """
      if redis.call('SISMEMBER', KEYS[1], ARGV[1]) == 1 then
        return 0
      else
        return redis.call('ZADD', KEYS[2], ARGV[2], ARGV[1])
      end
    """;

    private static final String ISSUE_LUA = """
        local cur = redis.call('ZSCORE', KEYS[1], ARGV[1])
        if (not cur) or (tonumber(cur) < 0) then
          return 0
        end
        redis.call('ZREM', KEYS[1], ARGV[1])
        redis.call('SADD', KEYS[2], ARGV[1])
        return 1
    """;

    private static final RedisScript<Long> CALL_SCRIPT   =
            new DefaultRedisScript<>(CALL_LUA, Long.class);

    private static final RedisScript<Long> ISSUE_SCRIPT  =
            new DefaultRedisScript<>(ISSUE_LUA, Long.class);

    public boolean callIssue(Long userId, Long couponId) {
        String issuedKey = KEY_PREFIX + couponId + ISSUED_KEY_SUFFIX;
        String callKey   = KEY_PREFIX + couponId + CALL_KEY_SUFFIX;
        String member    = "userId:" + userId;
        String score     = String.valueOf(System.currentTimeMillis());
        Long result = redisTemplate.execute(
                CALL_SCRIPT,
                List.of(issuedKey, callKey),
                member, score
        );
        // result == 0L → 이미 발급된 유저
        // result == 1L → 신규 등록 성공
        return result == 1L;
    }

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
