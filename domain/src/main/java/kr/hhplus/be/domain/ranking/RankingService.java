package kr.hhplus.be.domain.ranking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final RankingRepository rankingRepository;

    @Transactional
    public void saveSalesProduct(RankingCommand.SaveSalesProduct command) {
        // 일간 랭킹 저장
        rankingRepository.saveSalesProduct(command.orderProducts(), command.orderDateTime());
    }

    public void saveDailyRanking(RankingCommand.SaveDailyRanking command) {
        rankingRepository.saveDailyRanking(command.targetDateTime());
    }

    public List<SalesProduct> findDailySalesProducts() {
        return rankingRepository.findDailySalesProducts();
    }

}
