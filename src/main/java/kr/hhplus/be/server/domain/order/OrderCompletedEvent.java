package kr.hhplus.be.server.domain.order;

public record OrderCompletedEvent(
        OrderInfo orderInfo
) {
}
