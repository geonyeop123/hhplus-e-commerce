package kr.hhplus.be.server.domain.stats;

import java.util.List;

public interface StatsRepository {
    List<PopularProduct> getPopularProducts();
}
