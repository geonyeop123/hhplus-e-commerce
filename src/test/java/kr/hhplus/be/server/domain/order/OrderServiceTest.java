package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductInfo;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.userCoupon.UserCouponInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문을 요청하면 생성 후 저장한다.")
    @Test
    void order() {
        // given
        User user = User.create("yeop");
        ProductInfo product = ProductInfo.from(Product.create( "사과", 50, 5000));
        List<OrderCommand.OrderLine> orderLines =
                List.of(new OrderCommand.OrderLine(product, 1));

        OrderCommand.Create command = new OrderCommand.Create(user, UserCouponInfo.empty(), orderLines);

        when(orderRepository.save(any(Order.class))).thenReturn(any(Order.class));

        // when
        orderService.order(command);

        // then
        verify(orderRepository, times(1)).save(any(Order.class));
    }

}