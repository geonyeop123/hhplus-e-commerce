package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.order.Order;

public record PaymentCommand(
) {
    public record Pay(
            Order order,
            Long userId
    ) {

    }
}
