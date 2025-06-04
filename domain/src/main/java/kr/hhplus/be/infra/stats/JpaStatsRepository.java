package kr.hhplus.be.infra.stats;

import kr.hhplus.be.domain.stats.DailySalesProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaStatsRepository extends JpaRepository<DailySalesProduct, Long> {
}
