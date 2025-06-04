package kr.hhplus.be.infra.stats;

import kr.hhplus.be.domain.stats.DailySalesProduct;
import kr.hhplus.be.domain.stats.StatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StatsRepositoryImpl implements StatsRepository {

    private final JpaStatsRepository jpaStatsRepository;

    @Override
    public void saveAll(List<DailySalesProduct> list) {
        jpaStatsRepository.saveAll(list);
    }
}
