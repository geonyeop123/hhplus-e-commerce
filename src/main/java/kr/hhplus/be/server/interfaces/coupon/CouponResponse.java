package kr.hhplus.be.server.interfaces.coupon;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CouponResponse(
        Long userId,
        Long userCouponId,
        String name,
        int discountAmount,
        LocalDate expirationAt,
        LocalDateTime usedAt
) {
}
