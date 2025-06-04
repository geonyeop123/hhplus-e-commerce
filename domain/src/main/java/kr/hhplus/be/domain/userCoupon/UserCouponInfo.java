package kr.hhplus.be.domain.userCoupon;

import kr.hhplus.be.domain.coupon.CouponType;
import kr.hhplus.be.domain.coupon.DiscountType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserCouponInfo(
        Long id,
        Long userId,
        String name,
        CouponType type,
        DiscountType discountType,
        int discountAmount,
        LocalDateTime usedAt,
        LocalDate expiredAt
) {
    public static UserCouponInfo from(UserCoupon userCoupon){
        return new UserCouponInfo(
                userCoupon.getId(), userCoupon.getUserId(), userCoupon.getName(),
                userCoupon.getType(), userCoupon.getDiscountType(), userCoupon.getDiscountAmount(), userCoupon.getUsedAt(), userCoupon.getExpiredAt()
        );
    }

    public static UserCouponInfo empty(){
        return new UserCouponInfo(null, null, null, null, null, 0, null, null);
    }
}
