package kr.hhplus.be.server.interfaces.order.request;

import java.util.List;

public record CreateOrderRequest(
        Long userId,
        Long userCouponId,
        List<OrderItem> orderItems
) {

    public record OrderItem(
            Long productId,
            int quantity
    ) {
    }
}
