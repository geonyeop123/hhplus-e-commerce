package kr.hhplus.be.infra.ranking;

import kr.hhplus.be.domain.order.OrderProduct;
import kr.hhplus.be.domain.ranking.RankingRepository;
import kr.hhplus.be.domain.ranking.SalesProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RankingRepositoryImpl implements RankingRepository {

    private final RedisRankingRepository redisRankingRepository;

    @Override
    public void saveSalesProduct(List<OrderProduct> orderProducts, LocalDateTime orderDateTime) {
        redisRankingRepository.saveSalesProduct(orderProducts, orderDateTime);
    }

    @Override
    public void saveDailyRanking(LocalDateTime targetDateTime){
        redisRankingRepository.saveDailyRanking(targetDateTime);
    }

    @Override
    public List<SalesProduct> findDailySalesProducts() {
        return redisRankingRepository.findDailySalesProducts();
    }
}
