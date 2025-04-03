package kr.hhplus.be.server.interfaces.order.response;

public record OrderResponse(
        Long orderId,
        Long paymentId,
        int orderTotalAmount,
        int paymentTotalAmount
) {
}
