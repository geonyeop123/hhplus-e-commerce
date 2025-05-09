package kr.hhplus.be.server.domain.stats;

import kr.hhplus.be.server.infra.stats.JpaStatsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest
@Transactional
class StatsServiceIntegrationTest {

    @Autowired
    private StatsService statsService;

    @Autowired
    private JpaStatsRepository jpaStatsRepository;

    @DisplayName("datetime을 기준으로 판매된 상품의 집계 데이터를 저장할 수 있다.")
    @Test
    @Sql(scripts = "/sql/saveSalesProduct.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void saveSalesProductsByDateTime() {
        // given
        LocalDateTime dateTime = LocalDateTime.now().minusDays(1);
        StatsCommand.SaveSalesProducts command = new StatsCommand.SaveSalesProducts(dateTime);

        // when
        statsService.saveSalesProductByDateTime(command);

        // then
        List<SalesProduct> all = jpaStatsRepository.findByOrderDateWithProduct(dateTime.toLocalDate());
        assertThat(all).hasSize(5);
        assertThat(all).extracting("product.id", "salesCount", "orderDate")
                .containsExactlyInAnyOrder(
                        tuple(1L, 1L, dateTime.toLocalDate()),
        tuple(2L, 2L, dateTime.toLocalDate()),
                tuple(3L, 3L, dateTime.toLocalDate()),
                tuple(4L, 4L, dateTime.toLocalDate()),
                tuple(5L, 5L, dateTime.toLocalDate()));
    }

    @DisplayName("3일간 가장 판매가 많았던 상품 5개를 조회할 수 있다.")
    @Test
    @Sql(scripts = "/sql/popularProduct.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
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