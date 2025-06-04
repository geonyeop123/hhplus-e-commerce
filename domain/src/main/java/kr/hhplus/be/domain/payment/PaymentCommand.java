package kr.hhplus.be.domain.payment;

import kr.hhplus.be.domain.order.Order;
import kr.hhplus.be.domain.user.User;

public record PaymentCommand(
) {
    public record Pay(
            Order order,
            User user
    ) {

    }
}
