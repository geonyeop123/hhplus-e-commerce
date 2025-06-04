package kr.hhplus.be.server.application.event.salesProducts;

import kr.hhplus.be.domain.order.*;
import kr.hhplus.be.domain.product.Product;
import kr.hhplus.be.domain.product.ProductInfo;
import kr.hhplus.be.domain.user.User;
import kr.hhplus.be.server.application.event.ranking.RankingEventListener;
import kr.hhplus.be.server.application.ranking.RankingCriteria;
import kr.hhplus.be.server.application.ranking.RankingFacade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RankingEventListenerTest {

    @Mock
    private RankingFacade rankingFacade;

    @InjectMocks
    private RankingEventListener listener;

    @DisplayName("주문 완료 event가 발행되면 listener는 statsService의 saveSalesProductByOrder를 호출한다.")
    @Test
    void orderCompletedEvent() {
        // given
        OrderProduct orderProduct = OrderProduct.create(ProductInfo.from(Product.create("사과", 1000, 10)), 2);
        Order order = Order.create(User.create("yeop"));
        order.addOrderProduct(orderProduct);
        OrderInfo orderInfo = OrderInfo.from(order);

        RankingCriteria criteria = new RankingCriteria(orderInfo);
        doNothing().when(rankingFacade).saveSalesProduct(criteria);

        OrderEvent.Completed event = new OrderEvent.Completed(orderInfo);
        // when
        listener.handleOrderCompleted(event);

        // then
        verify(rankingFacade).saveSalesProduct(criteria);
    }
    

}