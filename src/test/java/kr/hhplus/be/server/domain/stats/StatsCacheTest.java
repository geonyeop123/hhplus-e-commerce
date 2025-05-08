package kr.hhplus.be.server.domain.stats;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Transactional
public class StatsCacheTest {

    @Autowired
    private StatsService statsService;

    @Autowired
    private RedisCacheManager redisCacheManager;

    @BeforeEach
    void setUp() {
        // 캐시 초기화
        Cache cache = redisCacheManager.getCache("popularProducts");
        if (cache != null) {
            cache.clear();
        }
    }

    @DisplayName("해당 데이터를 최초로 조회하는 경우 캐시 데이터에 저장된다.")
    @Test
    @Sql(scripts = "/sql/popularProduct.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void saveCacheData() {
        // given
        LocalDate end = LocalDate.now().minusDays(1);
        LocalDate start = end.minusDays(2);
        // cache key 계산 (service 메서드의 @Cacheable key 전략과 동일하게)
        String cacheKey = start.toString() + "_" + end.toString();

        // when
        StatsCommand.PopularProducts command = new StatsCommand.PopularProducts(start, end);

        statsService.getPopularProducts(command);

        // 캐시에 데이터가 저장되었는지 확인
        Cache.ValueWrapper wrapper = redisCacheManager
                .getCache("popularProducts")
                .get(cacheKey);
        assertThat(wrapper).isNotNull();
        PopularProducts products = (PopularProducts)wrapper.get();
        assertThat(products).isNotNull();
        assertThat(products.getProducts()).isNotNull();
        assertThat(products.getProducts()).hasSize(5);
    }

}
