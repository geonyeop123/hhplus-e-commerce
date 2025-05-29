package kr.hhplus.be.domain.stats;

import java.util.List;

public interface StatsRepository {
    void saveAll(List<DailySalesProduct> list);
}
