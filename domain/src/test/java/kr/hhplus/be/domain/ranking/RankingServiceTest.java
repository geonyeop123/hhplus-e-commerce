package kr.hhplus.be.domain.ranking;

import kr.hhplus.be.domain.order.OrderProduct;
import kr.hhplus.be.domain.product.Product;
import kr.hhplus.be.domain.product.ProductInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RankingServiceTest {

    @Mock
    private RankingRepository rankingRepository;

    @InjectMocks
    private RankingService rankingService;

    @DisplayName("rankingService의 saveDailyRanking 호출되면 repository를 호출하여 해당 데이터를 저장한다.")
    @Test
    void saveSalesProductByOrder() {
        // given
        Product product = Product.create("사과", 1000, 10);
        List<OrderProduct> orderProducts = List.of(OrderProduct.create(ProductInfo.from(product), 2));
        LocalDateTime orderDateTime = LocalDateTime.of(2025, 5, 14, 0, 0, 0);
        RankingCommand.SaveSalesProduct command = new RankingCommand.SaveSalesProduct(orderProducts, orderDateTime);

        doNothing().when(rankingRepository).saveSalesProduct(orderProducts, orderDateTime);

        // when
        rankingService.saveSalesProduct(command);

        // then
        verify(rankingRepository, times(1)).saveSalesProduct(orderProducts, orderDateTime);
    }

    @DisplayName("일간 랭킹을 저장할 수 있다.")
    @Test
    void saveDailyRanking() {
        LocalDateTime targetDate = LocalDateTime.now();
        // given
        doNothing().when(rankingRepository).saveDailyRanking(targetDate);

        // when
        RankingCommand.SaveDailyRanking command = new RankingCommand.SaveDailyRanking(targetDate);
        rankingService.saveDailyRanking(command);

        // then
        verify(rankingRepository, times(1)).saveDailyRanking(targetDate);
    }

    @DisplayName("일간 랭킹의 product id 목록을 조회할 수 있다.")
    @Test
    void findDailyRankingProductIds() {
        // given
        List<SalesProduct> ids = List.of(SalesProduct.create(1L, 1), SalesProduct.create(2L, 1));
        when(rankingRepository.findDailySalesProducts()).thenReturn(ids);
        // when
        List<SalesProduct> dailySalesProduct = rankingService.findDailySalesProducts();

        // then
        verify(rankingRepository, times(1)).findDailySalesProducts();
    }

}