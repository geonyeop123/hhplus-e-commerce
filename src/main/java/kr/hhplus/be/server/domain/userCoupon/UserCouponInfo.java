package kr.hhplus.be.server.domain.userCoupon;

import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.domain.coupon.DiscountType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserCouponInfo(
        Long id,
        Long userId,
        Long orderId,
        String name,
        CouponType type,
        DiscountType discountType,
        int discountAmount,
        LocalDateTime usedAt,
        LocalDate expiredAt
) {
    public static UserCouponInfo from(UserCoupon userCoupon){
        return new UserCouponInfo(
                userCoupon.getId(), userCoupon.getUserId(), userCoupon.getOrderId(), userCoupon.getName(),
                userCoupon.getType(), userCoupon.getDiscountType(), userCoupon.getDiscountAmount(), userCoupon.getUsedAt(), userCoupon.getExpiredAt()
        );
    }
}
