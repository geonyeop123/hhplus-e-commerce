package kr.hhplus.be.domain.ranking;

import kr.hhplus.be.domain.order.OrderProduct;

import java.time.LocalDateTime;
import java.util.List;

public interface RankingRepository {
    void saveSalesProduct(List<OrderProduct> orderProducts, LocalDateTime orderDateTime);
    void saveDailyRanking(LocalDateTime targetDateTime);
    List<SalesProduct> findDailySalesProducts();
}
