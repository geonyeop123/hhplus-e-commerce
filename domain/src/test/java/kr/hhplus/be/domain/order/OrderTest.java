package kr.hhplus.be.domain.order;

import kr.hhplus.be.domain.product.Product;
import kr.hhplus.be.domain.product.ProductInfo;
import kr.hhplus.be.domain.user.User;
import kr.hhplus.be.domain.userCoupon.UserCoupon;
import kr.hhplus.be.domain.userCoupon.UserCouponInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    @DisplayName("user를 받아 Order를 생성하면, OrderStatus는 PENDING이다.")
    @Test
    void createOrder() {
        // given
        User user = User.create("yeop");

        // when
        Order order = Order.create(user);

        // then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
    }

    @DisplayName("order에 orderProduct를 추가할 수 있다.")
    @Test
    void addOrderProduct() {
        // given
        OrderProduct orderProduct = OrderProduct.create(makeProductInfo(1000), 1);
        Order order = Order.create(User.create("yeop"));

        // when
        order.addOrderProduct(orderProduct);

        // then
        assertThat(order.getOrderProducts()).hasSize(1);
    }

    @Nested
    class calculateTotalAmount{
        @DisplayName("orderProduct가 주문에 추가될 때마다 주문가격과 최종가격이 변동된다.")
        @Test
        void success() {
            // given
            int firstPrice = 5000;
            int secondPrice = 6000;

            OrderProduct orderProduct = OrderProduct.create(makeProductInfo(firstPrice), 1);
            OrderProduct orderProduct2 = OrderProduct.create(makeProductInfo(secondPrice), 1);

            Order order = Order.create(User.create("yeop"));

            // when
            order.addOrderProduct(orderProduct);
            order.addOrderProduct(orderProduct2);

            // then
            assertThat(order.getOrderAmount()).isEqualTo(firstPrice + secondPrice);
            assertThat(order.getFinalAmount()).isEqualTo(firstPrice + secondPrice);
        }

        @DisplayName("쿠폰을 적용하는 경우 최종가격에 할인 금액이 적용된다.")
        @Test
        void haveDiscountInfo() {
            // given
            int price = 5000;
            int discountAmount = 3000;
            UserCouponInfo couponInfo = UserCouponInfo.from(UserCoupon.builder().discountAmount(discountAmount).build());
            OrderProduct orderProduct = OrderProduct.create(makeProductInfo(price), 1);

            Order order = Order.create(User.create("yeop"));
            order.addOrderProduct(orderProduct);

            // when
            order.applyCoupon(couponInfo);

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
            UserCouponInfo couponInfo = UserCouponInfo.from(UserCoupon.builder().discountAmount(discountAmount).build());
            OrderProduct orderProduct = OrderProduct.create(makeProductInfo(price), 1);

            Order order = Order.create(User.create("yeop"));
            order.addOrderProduct(orderProduct);

            // when
            order.applyCoupon(couponInfo);

            // then
            assertThat(order.getOrderAmount()).isEqualTo(price);
            assertThat(order.getFinalAmount()).isEqualTo(0);
        }
    }

    @DisplayName("complete로 order status를 SUCCESS로 변경할 수 있다.")
    @Test
    void test() {
        // given
        Order order = Order.create(User.create("yeop"));

        // when
        order.complete();

        // then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.SUCCESS);
    }

    private static ProductInfo makeProductInfo(int price) {
        return ProductInfo.from(Product.create( "사과", 50, price));
    }
}