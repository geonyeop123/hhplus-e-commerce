package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.order.Order;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Payment {

    private Long id;
    private Order order;
    private PaymentStatus status;
    private int totalAmount;
    private LocalDateTime paymentDateTime;

    public static Payment createByOrder(Order order) {
        return new Payment(order);
    }

    public void complete(LocalDateTime paymentDateTime) {
        this.status = PaymentStatus.SUCCESS;
        this.paymentDateTime = paymentDateTime;
    }

    private Payment(Order order) {
        this.order = order;
        this.totalAmount = order.getPaymentAmount();
        this.status = PaymentStatus.PENDING;
    }
}
