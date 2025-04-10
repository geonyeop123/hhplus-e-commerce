package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.order.Order;
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
        Order order = Order.create(user);
        // when
        Payment payment = Payment.createByOrder(order);

        // then
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PENDING);
    }


    private static Product makeProduct(Long productId, int price) {
        return Product.create(productId, "사과", 50, price);
    }

}