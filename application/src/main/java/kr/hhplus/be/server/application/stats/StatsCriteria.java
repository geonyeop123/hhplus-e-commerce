package kr.hhplus.be.server.application.stats;

import java.time.LocalDate;

public record StatsCriteria(
        LocalDate orderDate
) {
}
