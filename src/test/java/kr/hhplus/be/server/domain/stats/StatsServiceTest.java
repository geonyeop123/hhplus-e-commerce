package kr.hhplus.be.server.domain.stats;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @Mock
    private StatsRepository statsRepository;

    @InjectMocks
    private StatsService statsService;

    @DisplayName("3일간 가장 많이 팔린 상품 5개를 조회할 수 있다.")
    @Test
    void test() {
        // given
        List<PopularProduct> res = List.of(new PopularProduct(1L, 45L, "맥북", 5000, 50),
                new PopularProduct(2L, 45L, "맥북", 5000, 50),
                new PopularProduct(3L, 45L, "맥북", 5000, 50),
                new PopularProduct(4L, 45L, "맥북", 5000, 50),
                new PopularProduct(5L, 45L, "맥북", 5000, 50));

        when(statsRepository.getPopularProducts()).thenReturn(res);

        // when
        List<PopularProduct> popularProducts = statsService.getPopularProducts();

        // then
        assertThat(popularProducts).isNotNull();
        verify(statsRepository, times(1)).getPopularProducts();
    }

}