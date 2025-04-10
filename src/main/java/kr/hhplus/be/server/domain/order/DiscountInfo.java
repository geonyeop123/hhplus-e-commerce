package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.userCoupon.UserCoupon;

public record DiscountInfo(
        int discountAmount
) {
    public static DiscountInfo from(UserCoupon userCoupon) {
        return new DiscountInfo(userCoupon.getDiscountAmount());
    }

    public static DiscountInfo empty() {
        return new DiscountInfo(0);
    }
}
