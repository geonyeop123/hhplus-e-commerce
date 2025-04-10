package kr.hhplus.be.server.application.order;

public record OrderResult(
        Long orderId,
        Long paymentId,
        int orderTotalAmount,
        int paymentTotalAmount
) {

}
