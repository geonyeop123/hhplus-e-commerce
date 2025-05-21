package kr.hhplus.be.server.application.event.dataPlatform;

import kr.hhplus.be.server.domain.dataplatform.DataPlatformCommand;
import kr.hhplus.be.server.domain.dataplatform.DataPlatformService;
import kr.hhplus.be.server.domain.order.OrderCompletedEvent;
import kr.hhplus.be.server.domain.order.OrderInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class DataPlatformEventListener {

    private final DataPlatformService dataPlatformService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleOrderCompleted(OrderCompletedEvent event) {
        OrderInfo orderInfo = event.orderInfo();
        dataPlatformService.send(new DataPlatformCommand(orderInfo));
    }

}
