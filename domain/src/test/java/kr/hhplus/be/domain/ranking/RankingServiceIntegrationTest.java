package kr.hhplus.be.domain.ranking;

import kr.hhplus.be.domain.order.OrderProduct;
import kr.hhplus.be.domain.product.Product;
import kr.hhplus.be.domain.product.ProductInfo;
import kr.hhplus.be.infra.product.JpaProductRepository;
import kr.hhplus.be.infra.ranking.RedisRankingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create"
})
@Transactional
class RankingServiceIntegrationTest {

    @Autowired
    private JpaProductRepository jpaProductRepository;

    @Autowired
    private RedisRankingRepository redisRankingRepository;

    @Autowired
    private RankingService rankingService;

    @Autowired
    private RedisCacheManager redisCacheManager;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @AfterEach
    void tearDown() {
        redisCacheManager.getCacheNames().forEach(cacheName -> Objects.requireNonNull(redisCacheManager.getCache(cacheName)).clear());
        redisTemplate.delete(redisTemplate.keys("*"));
    }

    @DisplayName("saveSalesProductByOrder를 호출하면 해당 OrderProduct를 Redis에 ZSet으로 저장한다.")
    @Test
    void saveSalesProductByOrder() {
        // given
        LocalDateTime orderDateTime = LocalDateTime.now();
        String key = "salesProduct::" + orderDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHH"));
        Product product = Product.create("사과", 1000, 10);
        jpaProductRepository.save(product);
        List<OrderProduct> orderProducts = List.of(OrderProduct.create(ProductInfo.from(product), 2));
        RankingCommand.SaveSalesProduct command = new RankingCommand.SaveSalesProduct(orderProducts, orderDateTime);

        // when
        rankingService.saveSalesProduct(command);

        // then
        assertThat(redisTemplate.hasKey(key)).isTrue();
        Set<ZSetOperations.TypedTuple<Object>> typedTuples = redisTemplate.opsForZSet().rangeWithScores(key, 0, -1);
        assertThat(typedTuples).hasSize(1);
        assertThat(typedTuples).extracting("score", "value")
                .containsExactly(tuple((double)orderProducts.get(0).getQuantity()
                        , "product::" + product.getId()));
    }

    @DisplayName("같은 시간대에 동일한 상품의 주문이 2번 발생한 경우 score 값이 증가한다.")
    @Test
    void saveSalesProductByOrderSameProduct() {
        // given
        LocalDateTime orderDateTime = LocalDateTime.now();
        String key = "salesProduct::" + orderDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHH"));
        Product product = Product.create("사과", 1000, 10);
        jpaProductRepository.save(product);
        List<OrderProduct> orderProducts = List.of(OrderProduct.create(ProductInfo.from(product), 2));
        RankingCommand.SaveSalesProduct command = new RankingCommand.SaveSalesProduct(orderProducts, orderDateTime);

        // when
        rankingService.saveSalesProduct(command);
        rankingService.saveSalesProduct(command);

        // then
        assertThat(redisTemplate.hasKey(key)).isTrue();
        Set<ZSetOperations.TypedTuple<Object>> typedTuples = redisTemplate.opsForZSet().rangeWithScores(key, 0, -1);
        assertThat(typedTuples).hasSize(1);
        assertThat(typedTuples).extracting("score", "value")
                .containsExactly(tuple(4.0, "product::" + product.getId()));
    }

    @DisplayName("다른 시간대에 동일한 상품의 주문이 2번 발생한 경우 별도의 key로 각각 저장된다.")
    @Test
    void saveSalesProductByOrderDifferentTime() {
        // given
        LocalDateTime firstOrderDateTime = LocalDateTime.now();
        LocalDateTime secondOrderDateTime = LocalDateTime.now().plusHours(1);
        String firstKey = "salesProduct::" + firstOrderDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHH"));
        String secondKey = "salesProduct::" + secondOrderDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHH"));
        Product product = Product.create("사과", 1000, 10);
        jpaProductRepository.save(product);
        List<OrderProduct> orderProducts = List.of(OrderProduct.create(ProductInfo.from(product), 2));
        RankingCommand.SaveSalesProduct firstCommand = new RankingCommand.SaveSalesProduct(orderProducts, firstOrderDateTime);
        RankingCommand.SaveSalesProduct secondCommand = new RankingCommand.SaveSalesProduct(orderProducts, secondOrderDateTime);

        // when
        rankingService.saveSalesProduct(firstCommand);
        rankingService.saveSalesProduct(secondCommand);

        // then
        assertThat(redisTemplate.hasKey(firstKey)).isTrue();
        assertThat(redisTemplate.hasKey(secondKey)).isTrue();
        Set<ZSetOperations.TypedTuple<Object>> firstTuples = redisTemplate.opsForZSet().rangeWithScores(firstKey, 0, -1);
        assertThat(firstTuples).hasSize(1);
        assertThat(firstTuples).extracting("score", "value")
                .containsExactly(tuple(2.0, "product::" + product.getId()));
        Set<ZSetOperations.TypedTuple<Object>> secondTuples = redisTemplate.opsForZSet().rangeWithScores(secondKey, 0, -1);
        assertThat(secondTuples).hasSize(1);
        assertThat(secondTuples).extracting("score", "value")
                .containsExactly(tuple(2.0, "product::" + product.getId()));
    }

    @DisplayName("")
    @Test
    void saveDailyRanking() {
        // given
        LocalDateTime orderDateTime = LocalDateTime.now().minusHours(2);
        Product product1 = Product.create("사과", 1000, 10);
        Product product2 = Product.create("배", 1000, 10);
        jpaProductRepository.saveAll(List.of(product1, product2));
        List<OrderProduct> orderProducts = List.of(OrderProduct.create(ProductInfo.from(product1), 2), OrderProduct.create(ProductInfo.from(product2), 1));
        redisRankingRepository.saveSalesProduct(orderProducts, orderDateTime);
        String DAILY_KEY = "popularProducts::daily";

        // when
        LocalDateTime targetDate = LocalDateTime.now();
        RankingCommand.SaveDailyRanking command = new RankingCommand.SaveDailyRanking(targetDate);
        rankingService.saveDailyRanking(command);

        // then
        Set<ZSetOperations.TypedTuple<Object>> tuples = redisTemplate.opsForZSet().reverseRangeWithScores(DAILY_KEY, 0, -1);
        assertThat(tuples).hasSize(2);
        assertThat(tuples).extracting("score", "value")
                .containsExactly(tuple(2.0, "product::" + product1.getId()), tuple(1.0, "product::" + product2.getId()));

    }

    @DisplayName("일간 순위의 product id과 판매 수량을 가진 SalesProduct의 목록을 조회할 수 있다.")
    @Test
    void findDailyRankingProductIds() {
        // given
        LocalDateTime orderDateTime = LocalDateTime.now().minusHours(2);
        Product product1 = Product.create("사과", 1000, 10);
        Product product2 = Product.create("배", 1000, 10);
        jpaProductRepository.saveAll(List.of(product1, product2));
        List<OrderProduct> orderProducts = List.of(OrderProduct.create(ProductInfo.from(product1), 2), OrderProduct.create(ProductInfo.from(product2), 1));
        redisRankingRepository.saveSalesProduct(orderProducts, orderDateTime);
        redisRankingRepository.saveDailyRanking(LocalDateTime.now());
        // when
        List<SalesProduct> salesProducts = rankingService.findDailySalesProducts();

        // then
        assertThat(salesProducts).hasSize(2);
    }

}