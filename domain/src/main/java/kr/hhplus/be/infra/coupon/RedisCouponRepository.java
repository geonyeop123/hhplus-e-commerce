package kr.hhplus.be.infra.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisCouponRepository {

    private final RedisTemplate<String, Object> redisTemplate;
}
