package kr.hhplus.be.domain.payment;

import kr.hhplus.be.domain.order.Order;
import kr.hhplus.be.domain.order.OrderProduct;
import kr.hhplus.be.domain.order.OrderRepository;
import kr.hhplus.be.domain.point.Point;
import kr.hhplus.be.domain.point.PointRepository;
import kr.hhplus.be.domain.product.Product;
import kr.hhplus.be.domain.product.ProductInfo;
import kr.hhplus.be.domain.user.User;
import kr.hhplus.be.infra.point.JpaPointRepository;
import kr.hhplus.be.infra.product.JpaProductRepository;
import kr.hhplus.be.infra.user.JpaUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create"
})
@Transactional
class PaymentServiceIntegrationTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private JpaProductRepository jpaProductRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private JpaPointRepository jpaPointRepository;

    @DisplayName("주문에 맞는 결제 금액대로 결제를 진행할 수 있다.")
    @Test
    void pay() {
        // given
        User user = jpaUserRepository.save(User.create("yeop"));
        jpaPointRepository.save(Point.create(user, 5000));
        ProductInfo productInfo = ProductInfo.from(jpaProductRepository.save(Product.create("사과", 50, 5000)));
        OrderProduct orderProduct = OrderProduct.create(productInfo, 1);
        Order order = Order.create(user);
        order.addOrderProduct(orderProduct);
        Order savedOrder = orderRepository.save(order);

        // when
        PaymentCommand.Pay command = new PaymentCommand.Pay(savedOrder, user);
        Payment pay = paymentService.pay(command);

        // then
        Point point = pointRepository.findByUserId(user.getId()).orElse(null);
        assertThat(point).isNotNull();
        assertThat(point.getBalance()).isEqualTo(0);
        assertThat(pay.getStatus()).isEqualTo(PaymentStatus.SUCCESS);
    }
}