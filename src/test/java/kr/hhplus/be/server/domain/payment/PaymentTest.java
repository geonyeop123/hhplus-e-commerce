package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.order.DiscountInfo;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderProduct;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentTest {

    @DisplayName("order를 받아 payment를 생성하면, PaymentStauts는 PENDING이다.")
    @Test
    void createByOrder() {
        // given
        User user = User.create(1L, "yeop");
        Order order = Order.create(user, DiscountInfo.empty());
        // when
        Payment payment = Payment.createByOrder(order);

        // then
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PENDING);
    }

    @DisplayName("payment의 totalAmount는 order의 finalAmount이다.")
    @Test
    void totalAmountEqualToFinalAmount() {
        // given
        User user = User.create(1L, "yeop");
        Order order = Order.create(user, DiscountInfo.empty());
        order.addOrderProduct(OrderProduct.create(makeProduct(1L, 5000), 1));
        order.calculateTotalAmount();
        // when
        Payment payment = Payment.createByOrder(order);

        // then
        assertThat(payment.getTotalAmount()).isEqualTo(order.getFinalAmount());
    }


    private static Product makeProduct(Long productId, int price) {
        return Product.create(productId, "사과", 50, price);
    }

}