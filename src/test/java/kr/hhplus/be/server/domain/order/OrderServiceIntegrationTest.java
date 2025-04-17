package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductInfo;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.userCoupon.UserCouponInfo;
import kr.hhplus.be.server.infra.order.JpaOrderRepository;
import kr.hhplus.be.server.infra.product.JpaProductRepository;
import kr.hhplus.be.server.infra.user.JpaUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@SpringBootTest
@Transactional
class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private JpaOrderRepository jpaOrderRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private JpaProductRepository jpaProductRepository;

    @DisplayName("주문을 요청하면 생성 후 저장되며, orderProduct도 저장된다.")
    @Test
    void order() {
        // given
        User user = User.create("user");
        User savedUser = jpaUserRepository.save(user);

        UserCouponInfo userCouponInfo = UserCouponInfo.empty();

        ProductInfo product1 =
                ProductInfo.from(jpaProductRepository.save(Product.create("사과", 50, 5000)));
        ProductInfo product2 =
                ProductInfo.from(jpaProductRepository.save(Product.create("배", 50, 4000)));


        List<OrderCommand.OrderLine> orderLines = List.of(
                new OrderCommand.OrderLine(product1, 1),
                new OrderCommand.OrderLine(product2, 2)
        );

        OrderCommand.Create command = new OrderCommand.Create(savedUser, userCouponInfo, orderLines);

        // when
        Order order = orderService.order(command);

        // then
        Order savedOrder = jpaOrderRepository.findById(order.getId())
                .orElse(null);
        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getId()).isEqualTo(order.getId());
        assertThat(savedOrder.getOrderAmount()).isEqualTo(13000);
        assertThat(savedOrder.getStatus()).isEqualTo(OrderStatus.SUCCESS);

        List<OrderProduct> orderProducts = savedOrder.getOrderProducts();
        assertThat(orderProducts).hasSize(2);
        assertThat(orderProducts).extracting("price", "quantity")
                .containsExactlyInAnyOrder(
                        tuple(5000, 1),
                        tuple(4000, 2)
                );
    }

}