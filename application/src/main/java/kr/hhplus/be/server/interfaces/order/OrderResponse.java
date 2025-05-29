package kr.hhplus.be.server.interfaces.order;

import kr.hhplus.be.server.application.order.OrderResult;

public record OrderResponse(
        Long orderId,
        Long paymentId,
        int orderTotalAmount,
        int paymentTotalAmount
) {
    public static OrderResponse from(OrderResult orderResult){
        return new OrderResponse(orderResult.orderId(), orderResult.paymentId(), orderResult.orderTotalAmount(), orderResult.paymentTotalAmount());
    }
}
