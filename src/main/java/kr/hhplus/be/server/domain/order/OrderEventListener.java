package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.common.GlobalEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderEventListener {

    private final GlobalEventPublisher globalEventPublisher;


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleOrderCompleted(OrderCompletedEvent event) {
        globalEventPublisher.publish("outside.order.completed.v1", event.orderInfo());
    }

}
