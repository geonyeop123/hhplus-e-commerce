package kr.hhplus.be.server.interfaces.coupon;

import kr.hhplus.be.domain.userCoupon.UserCoupon;
import kr.hhplus.be.server.application.coupon.CouponResult;

import java.time.LocalDate;

public record CouponResponse(
        Long userCouponId,
        String name,
        int discountAmount,
        LocalDate expirationAt
) {

    public static CouponResponse from(UserCoupon userCoupon) {
        return new CouponResponse(userCoupon.getId(), userCoupon.getName(), userCoupon.getDiscountAmount(), userCoupon.getExpiredAt());
    }

    public static CouponResponse from(CouponResult.IssueUserCoupon result) {
        return new CouponResponse(result.id(), result.name(), result.discountAmount(), result.expiredAt());
    }
}
