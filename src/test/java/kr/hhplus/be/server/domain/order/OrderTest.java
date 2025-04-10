package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.userCoupon.UserCoupon;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    @DisplayName("user를 받아 Order를 생성하면, OrderStatus는 PENDING이다.")
    @Test
    void createOrder() {
        // given
        User user = User.create(1L, "yeop");

        // when
        Order order = Order.create(user, DiscountInfo.empty());

        // then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
    }

    @DisplayName("order에 orderProduct를 추가할 시 orderProduct의 order값이 해당 order가 된다.")
    @Test
    void addOrderProduct() {
        // given
        OrderProduct orderProduct = OrderProduct.create(makeProductById(1L), 1);
        Order order = Order.create(User.create(1L, "yeop"), DiscountInfo.empty());

        // when
        order.addOrderProduct(orderProduct);

        // then
        assertThat(orderProduct.getOrder()).isEqualTo(order);
        assertThat(order.getOrderProducts()).hasSize(1);
    }

    @Nested
    class calculateTotalAmount{
        @DisplayName("calculateTotalAmount 호출을 통해 주문가격과 최종가격이 계산된다.")
        @Test
        void success() {
            // given
            int firstPrice = 5000;
            int secondPrice = 6000;

            DiscountInfo discountInfo = DiscountInfo.empty();
            OrderProduct orderProduct = OrderProduct.create(makeProduct(1L, firstPrice), 1);
            OrderProduct orderProduct2 = OrderProduct.create(makeProduct(2L, secondPrice), 1);

            Order order = Order.create(User.create(1L, "yeop"), discountInfo);
            order.addOrderProduct(orderProduct);
            order.addOrderProduct(orderProduct2);

            // when
            order.calculateTotalAmount();

            // then
            assertThat(order.getOrderAmount()).isEqualTo(firstPrice + secondPrice);
            assertThat(order.getFinalAmount()).isEqualTo(firstPrice + secondPrice);
        }

        @DisplayName("discountInfo의 값이 있는 경우, finalAmount의 값이 discountInfo의 discountAmount 값을 뺀 값이 된다.")
        @Test
        void haveDiscountInfo() {
            // given
            int price = 5000;
            int discountAmount = 3000;
            DiscountInfo discountInfo = DiscountInfo.from(UserCoupon.builder().discountAmount(discountAmount).build());
            OrderProduct orderProduct = OrderProduct.create(makeProduct(1L, price), 1);

            Order order = Order.create(User.create(1L, "yeop"), discountInfo);
            order.addOrderProduct(orderProduct);

            // when
            order.calculateTotalAmount();

            // then
            assertThat(order.getOrderAmount()).isEqualTo(price);
            assertThat(order.getFinalAmount()).isEqualTo(price - discountAmount);
        }

        @DisplayName("할인금액이 주문금액보다 높은 경우 최종 금액은 0원이 된다.")
        @Test
        void overDiscount() {
            // given
            int price = 5000;
            int discountAmount = 6000;
            DiscountInfo discountInfo = DiscountInfo.from(UserCoupon.builder().discountAmount(discountAmount).build());
            OrderProduct orderProduct = OrderProduct.create(makeProduct(1L, price), 1);

            Order order = Order.create(User.create(1L, "yeop"), discountInfo);
            order.addOrderProduct(orderProduct);

            // when
            order.calculateTotalAmount();

            // then
            assertThat(order.getOrderAmount()).isEqualTo(price);
            assertThat(order.getFinalAmount()).isEqualTo(0);
        }
    }

    @DisplayName("calculateTotalAmount를 호출하면 총 가격을 계산하여 totalAmount에 담는다.")
    @Test
    void calculateTotalAmount() {
        // given
        int firstPrice = 5000;
        int secondPrice = 6000;

        OrderProduct orderProduct = OrderProduct.create(makeProduct(1L, firstPrice), 1);
        OrderProduct orderProduct2 = OrderProduct.create(makeProduct(2L, secondPrice), 1);

        Order order = Order.create(User.create(1L, "yeop"), DiscountInfo.empty());
        order.addOrderProduct(orderProduct);
        order.addOrderProduct(orderProduct2);

        // when
        order.calculateTotalAmount();

        // then
        assertThat(order.getOrderAmount()).isEqualTo(firstPrice + secondPrice);
    }

    @DisplayName("complete로 order status를 SUCCESS로 변경할 수 있다.")
    @Test
    void test() {
        // given
        Order order = Order.create(User.create(1L, "yeop"), DiscountInfo.empty());

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