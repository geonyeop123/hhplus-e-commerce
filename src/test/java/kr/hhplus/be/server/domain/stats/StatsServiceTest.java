package kr.hhplus.be.server.domain.stats;

import kr.hhplus.be.server.infra.stats.SalesProductSummary;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @Mock
    private StatsRepository statsRepository;

    @InjectMocks
    private StatsService statsService;

    // ProjectionFactory 생성
    ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @DisplayName("날짜를 받아 판매 상품을 집계해 데이터를 저장할 수 있다.")
    @Test
    void saveSalesProductByDateTime() {
        // given
        LocalDateTime dateTime = LocalDateTime.of(2025, 4, 24, 1, 0, 0);
        SalesProductSummary p1 = factory.createProjection(SalesProductSummary.class, Map.of(
                "productId", 100L,
                "salesCount", 42L,
                "orderDate", LocalDate.of(2025, 4, 24)
        ));
        List<SalesProductSummary> products = List.of(p1);

        when(statsRepository.findSalesProductSummaryByDateTime(dateTime)).thenReturn(products);
        // when
        StatsCommand.SaveSalesProducts command = new StatsCommand.SaveSalesProducts(dateTime);
        statsService.saveSalesProductByDateTime(command);
        // then
        verify(statsRepository, times(1)).findSalesProductSummaryByDateTime(dateTime);
        verify(statsRepository, times(1)).batchInsert(products);
    }

    @DisplayName("3일간 가장 많이 팔린 상품 5개를 조회할 수 있다.")
    @Test
    void test() {
        // given
        List<PopularProduct> res = List.of(new PopularProduct(1L, 45L, "맥북", 5000, 50),
                new PopularProduct(2L, 45L, "맥북", 5000, 50),
                new PopularProduct(3L, 45L, "맥북", 5000, 50),
                new PopularProduct(4L, 45L, "맥북", 5000, 50),
                new PopularProduct(5L, 45L, "맥북", 5000, 50));
        StatsCommand.PopularProducts command = new StatsCommand.PopularProducts(LocalDate.now().minusDays(3), LocalDate.now().minusDays(1));
        when(statsRepository.getPopularProducts(command.startDate(), command.endDate())).thenReturn(res);

        // when
        PopularProducts popularProducts = statsService.getPopularProducts(command);

        // then
        assertThat(popularProducts).isNotNull();
        verify(statsRepository, times(1)).getPopularProducts(command.startDate(), command.endDate());
    }

}