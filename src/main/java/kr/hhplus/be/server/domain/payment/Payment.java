package kr.hhplus.be.server.domain.payment;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.order.Order;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
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
        this.totalAmount = order.getFinalAmount();
        this.status = PaymentStatus.PENDING;
    }
}
