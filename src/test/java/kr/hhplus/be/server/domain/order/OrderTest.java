package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    @DisplayName("user를 받아 Order를 생성하면, OrderStatus는 PENDING이다.")
    @Test
    void createOrder() {
        // given
        User user = User.create(1L, "yeop");

        // when
        Order order = Order.create(user);

        // then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
    }

    @DisplayName("order에 orderProduct를 추가할 시 orderProduct의 order값이 해당 order가 된다.")
    @Test
    void addOrderProduct() {
        // given
        OrderProduct orderProduct = OrderProduct.create(makeProductById(1L), 1);
        Order order = Order.create(User.create(1L, "yeop"));

        // when
        order.addOrderProduct(orderProduct);

        // then
        assertThat(orderProduct.getOrder()).isEqualTo(order);
        assertThat(order.getOrderProducts()).hasSize(1);
    }

    @DisplayName("calculateTotalAmount를 호출하면 총 가격을 계산하여 totalAmount에 담는다.")
    @Test
    void calculateTotalAmount() {
        // given
        int firstPrice = 5000;
        int secondPrice = 6000;

        OrderProduct orderProduct = OrderProduct.create(makeProduct(1L, firstPrice), 1);
        OrderProduct orderProduct2 = OrderProduct.create(makeProduct(2L, secondPrice), 1);

        Order order = Order.create(User.create(1L, "yeop"));
        order.addOrderProduct(orderProduct);
        order.addOrderProduct(orderProduct2);

        // when
        order.calculateTotalAmount();

        // then
        assertThat(order.getTotalOrderAmount()).isEqualTo(firstPrice + secondPrice);
    }

    @DisplayName("complete로 order status를 SUCCESS로 변경할 수 있다.")
    @Test
    void test() {
        // given
        Order order = Order.create(User.create(1L, "yeop"));

        // when
        order.complete();

        // then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.SUCCESS);
    }

    private static Product makeProduct(Long productId, int price) {
        return Product.create(productId, "사과", 50, price);
    }

    private static Product makeProductById(Long productid) {
        return makeProduct(productid, 5000);
    }


}