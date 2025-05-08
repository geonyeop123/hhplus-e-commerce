package kr.hhplus.be.server.domain.stats;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record StatsCommand() {

    public record PopularProducts(
            LocalDate startDate,
            LocalDate endDate
    ) {

    }

    public record SaveSalesProducts (
            LocalDateTime dateTime
    ) {
        public SaveSalesProducts {
            if (dateTime == null) {
                throw new IllegalArgumentException("날짜 값이 입력되지 않았습니다.");
            }
        }
    }
}
