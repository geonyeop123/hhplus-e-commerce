package kr.hhplus.be.server.application.event.ranking;

import kr.hhplus.be.server.application.ranking.RankingCriteria;
import kr.hhplus.be.server.application.ranking.RankingFacade;
import kr.hhplus.be.server.domain.order.OrderCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class RankingEventListener {

    private final RankingFacade rankingFacade;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleOrderCompleted(OrderCompletedEvent event) {
        RankingCriteria criteria = new RankingCriteria(event.orderInfo());
        rankingFacade.saveSalesProduct(criteria);
    }
}