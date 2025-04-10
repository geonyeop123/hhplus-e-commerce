package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderProductTest {

    @DisplayName("create로 생성할 시 orderProduct의 price는 파라미터 product의 price와 동일한 값을 가진다.")
    @Test
    void createOrderProduct() {
        // given
        Product product = Product.create(1L, "사과", 50, 5000);

        // when
        OrderProduct orderProduct = OrderProduct.create(product, 1);

        // then
        assertThat(orderProduct.getPrice()).isEqualTo(product.getPrice());
    }

    @DisplayName("setOrder로 order를 세팅할 수 있다.")
    @Test
    void setOrder() {
        // given
        Product product = Product.create(1L, "사과", 50, 5000);
        OrderProduct orderProduct = OrderProduct.create(product, 1);
        Order order = Order.create(User.create(1L, "yeop"));
        // when
        orderProduct.setOrder(order);

        // then
        assertThat(orderProduct.getOrder()).isEqualTo(order);
    }

}