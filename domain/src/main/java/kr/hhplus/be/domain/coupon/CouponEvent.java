package kr.hhplus.be.domain.coupon;

import java.time.LocalDateTime;

public record CouponEvent() {
    public record IssueCalled(
            Long couponId, Long userId, LocalDateTime callDateTime
    ) {

    }
}
