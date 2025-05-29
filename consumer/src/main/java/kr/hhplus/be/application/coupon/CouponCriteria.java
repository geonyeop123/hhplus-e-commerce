package kr.hhplus.be.application.coupon;


public record CouponCriteria(
) {
    public record IssueUserCoupon(Long userId, Long couponId) {
    }

    public record Issue(
            Long couponId
    ) {
    }
}
