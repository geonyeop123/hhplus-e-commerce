package kr.hhplus.be.server.interfaces.stats;

import kr.hhplus.be.server.domain.stats.StatsCommand;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record StatsRequest(
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
) {
    public StatsCommand.PopularProducts toCommand() {
        return new StatsCommand.PopularProducts(startDate, endDate);
    }
}
