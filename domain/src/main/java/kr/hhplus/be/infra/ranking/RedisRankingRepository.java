package kr.hhplus.be.infra.ranking;

import kr.hhplus.be.domain.order.OrderProduct;
import kr.hhplus.be.domain.ranking.SalesProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisRankingRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final DateTimeFormatter YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter YYYY_MM_DD_HH = DateTimeFormatter.ofPattern("yyyyMMddHH");
    private static final String SALES_KEY_PREFIX = "salesProduct::";
    private static final String PRODUCT_KEY_PREFIX = "product::";
    private static final String DAILY_KEY = "popularProducts::daily";

    public void saveSalesProduct(List<OrderProduct> orderProducts, LocalDateTime orderDateTime) {
        // key는 yyyyMMddHH로 2025051401로 시간단위로 표시된다.
        String key = SALES_KEY_PREFIX + orderDateTime.format(YYYY_MM_DD_HH);

        for (OrderProduct orderProduct : orderProducts) {
            String value = PRODUCT_KEY_PREFIX + orderProduct.getProductId();
            redisTemplate.opsForZSet().incrementScore(key, value, orderProduct.getQuantity());
        }

        setSalesProductExpire(key, orderDateTime);
    }

    private void setSalesProductExpire(String key, LocalDateTime orderDateTime) {
        LocalDateTime expireAtLdt = orderDateTime.plusHours(25)
                .truncatedTo(ChronoUnit.HOURS);

        Instant expireInstant = expireAtLdt
                .atZone(ZoneId.of("Asia/Seoul"))
                .toInstant();

        if(hasExpire(key)){
            redisTemplate.expireAt(key, expireInstant);
        }
    }

    private boolean hasExpire(String key){
        return redisTemplate.getExpire(key) < 0;
    }

    public void saveDailyRanking(LocalDateTime targetDateTime) {

        LocalDateTime startDateTime = targetDateTime.minusHours(24);
        String firstKey = SALES_KEY_PREFIX + startDateTime.format(YYYY_MM_DD_HH);

        List<String> otherKeys = new ArrayList<>();
        for( int i = 1; i < 24; i++){
            otherKeys.add(SALES_KEY_PREFIX + startDateTime.plusHours(i).format(YYYY_MM_DD_HH));
        }

        redisTemplate.opsForZSet().unionAndStore(firstKey, otherKeys, DAILY_KEY);
        redisTemplate.expire(DAILY_KEY, 70, TimeUnit.MINUTES);
    }

    public List<SalesProduct> findDailySalesProducts() {

        Set<ZSetOperations.TypedTuple<Object>> typedTuples =
                redisTemplate.opsForZSet().reverseRangeWithScores(DAILY_KEY, 0, -1);

        if(typedTuples == null || typedTuples.isEmpty()){
            return List.of();
        }

        return typedTuples.stream().map(tuple -> {
            String key = String.valueOf(tuple.getValue());
            Long productId = Long.parseLong(key.split(PRODUCT_KEY_PREFIX)[1]);
            int sales = Objects.requireNonNull(tuple.getScore()).intValue();
            return SalesProduct.create(productId, sales);
        }).toList();
    }
}
