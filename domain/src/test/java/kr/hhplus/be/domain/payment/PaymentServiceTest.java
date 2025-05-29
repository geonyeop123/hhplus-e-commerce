package kr.hhplus.be.domain.payment;

import kr.hhplus.be.domain.order.Order;
import kr.hhplus.be.domain.order.OrderProduct;
import kr.hhplus.be.domain.point.Point;
import kr.hhplus.be.domain.point.PointCommand;
import kr.hhplus.be.domain.point.PointService;
import kr.hhplus.be.domain.product.Product;
import kr.hhplus.be.domain.product.ProductInfo;
import kr.hhplus.be.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PointService pointService;

    @InjectMocks
    private PaymentService paymentService;

    @DisplayName("포인트로 결제를 진행할 수 있다.")
    @Test
    void pay() {
        // given
        User user = User.create("yeop");

        Order order = Order.create(user);
        order.addOrderProduct(OrderProduct.create(ProductInfo.from(Product.create("사과", 50, 5000)), 2));

        PointCommand.Use command = new PointCommand.Use(user, 10000);
        Point usedPoint = Point.create(user, 0);

        when(pointService.use(command)).thenReturn(usedPoint);
        when(paymentRepository.save(any(Payment.class))).thenReturn(any(Payment.class));

        PaymentCommand.Pay paymentCommand = new PaymentCommand.Pay(order, user);

        // when
        paymentService.pay(paymentCommand);

        // then
        verify(pointService, times(1)).use(command);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }
}