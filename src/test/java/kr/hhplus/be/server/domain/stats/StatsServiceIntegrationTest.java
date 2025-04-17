package kr.hhplus.be.server.domain.stats;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class StatsServiceIntegrationTest {

    @Autowired
    private StatsService statsService;

    @DisplayName("3일간 가장 판매가 많았던 상품 5개를 조회할 수 있다.")
    @Test
    @Sql(scripts = "/sql/popularProductTestData.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void popularProducts() {
        // given // when
        List<PopularProduct> popularProducts = statsService.getPopularProducts();

        // then
        assertThat(popularProducts).hasSize(5);

    }

    @DisplayName("3일간 가장 판매가 많았던 상품이 조회되지 않는 경우 빈 배열이 반환된다.")
    @Test
    void emptyPopularProducts() {
        // given // when
        List<PopularProduct> popularProducts = statsService.getPopularProducts();

        // then
        assertThat(popularProducts).isEmpty();
    }

}