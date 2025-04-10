package kr.hhplus.be.server.interfaces.coupon;

import kr.hhplus.be.server.domain.userCoupon.UserCoupon;

import java.time.LocalDate;

public record CouponResponse(
        Long userId,
        Long userCouponId,
        String name,
        int discountAmount,
        LocalDate expirationAt
) {
    public static CouponResponse from(UserCoupon userCoupon) {
        return new CouponResponse(userCoupon.getUserId(), userCoupon.getId(), userCoupon.getName(), userCoupon.getDiscountAmount(), userCoupon.getExpiredAt());
    }
}
