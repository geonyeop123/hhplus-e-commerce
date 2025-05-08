package kr.hhplus.be.server.domain.stats;

import kr.hhplus.be.server.infra.stats.SalesProductSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository statsRepository;

    @Transactional(readOnly = true)
    @Cacheable(
            cacheNames = "popularProducts",
            key        = "#command.startDate().toString() + '_' + #command.endDate().toString()"
    )
    public PopularProducts getPopularProducts(StatsCommand.PopularProducts command) {
        return new PopularProducts(statsRepository.getPopularProducts(command.startDate(), command.endDate()));
    }

    @Transactional
    public void saveSalesProductByDateTime(StatsCommand.SaveSalesProducts command) {
        List<SalesProductSummary> salesProducts = statsRepository.findSalesProductSummaryByDateTime(command.dateTime());
        statsRepository.batchInsert(salesProducts);
    }


}
