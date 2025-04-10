package kr.hhplus.be.server.domain.coupon;

public record CouponCommand(
        Long userId,
        Long couponId
) {

}
