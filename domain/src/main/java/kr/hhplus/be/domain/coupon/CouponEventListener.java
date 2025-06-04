package kr.hhplus.be.domain.coupon;

import kr.hhplus.be.domain.common.GlobalEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class CouponEventListener {

    private final GlobalEventPublisher globalEventPublisher;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleOrderCompleted(CouponEvent.IssueCalled event) {
        globalEventPublisher.publish(event.couponId().toString(), "outside.coupon.issueCalled.v1", event);
    }
}
