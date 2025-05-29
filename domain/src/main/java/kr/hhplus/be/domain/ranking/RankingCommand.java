package kr.hhplus.be.domain.ranking;

import kr.hhplus.be.domain.order.OrderProduct;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record RankingCommand() {
    public record SaveSalesProduct(
            List<OrderProduct> orderProducts,
            LocalDateTime orderDateTime
    ) {

    }

    public record SaveDailyRanking(LocalDateTime targetDateTime) {

    }

    public record FindDaily(LocalDate orderDate) {

    }
}
