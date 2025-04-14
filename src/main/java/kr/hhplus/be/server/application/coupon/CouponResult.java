package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.userCoupon.UserCoupon;

import java.time.LocalDate;

public record CouponResult(

) {
    public record IssueUserCoupon(
            Long id,
            String name,
            int discountAmount,
            LocalDate expiredAt
    ) {

        public static IssueUserCoupon from(UserCoupon userCoupon) {
            return new IssueUserCoupon(userCoupon.getId(), userCoupon.getName(), userCoupon.getDiscountAmount(), userCoupon.getExpiredAt());
        }
    }
}
