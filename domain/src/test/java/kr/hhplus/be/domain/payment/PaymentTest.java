package kr.hhplus.be.domain.payment;

import kr.hhplus.be.domain.order.Order;
import kr.hhplus.be.domain.order.OrderProduct;
import kr.hhplus.be.domain.product.Product;
import kr.hhplus.be.domain.product.ProductInfo;
import kr.hhplus.be.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentTest {

    @DisplayName("order를 받아 payment를 생성하면, PaymentStauts는 PENDING이다.")
    @Test
    void createByOrder() {
        // given
        User user = User.create("yeop");
        Order order = Order.create(user);
        // when
        Payment payment = Payment.createByOrder(order);

        // then
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PENDING);
    }

    @DisplayName("payment의 totalAmount는 order의 finalAmount이다.")
    @Test
    void totalAmountEqualToFinalAmount() {
        // given
        User user = User.create("yeop");
        Order order = Order.create(user);
        order.addOrderProduct(OrderProduct.create(makeProduct(5000), 1));
        // when
        Payment payment = Payment.createByOrder(order);

        // then
        assertThat(payment.getTotalAmount()).isEqualTo(order.getFinalAmount());
    }


    private static ProductInfo makeProduct(int price) {
        return ProductInfo.from(Product.create("사과", 50, price));
    }

}