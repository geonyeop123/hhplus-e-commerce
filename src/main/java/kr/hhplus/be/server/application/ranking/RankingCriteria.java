package kr.hhplus.be.server.application.ranking;

import kr.hhplus.be.server.domain.order.OrderInfo;

public record RankingCriteria(
        OrderInfo orderInfo
) {

}
