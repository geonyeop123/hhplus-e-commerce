package kr.hhplus.be.server.domain.coupon;

public record CouponInfo(
        Long id,
        String name,
        CouponType type,
        DiscountType discountType,
        int discountAmount,
        int expirationMonth
) {
    public static CouponInfo from(Coupon coupon) {
        return new CouponInfo(coupon.getId(), coupon.getName(), coupon.getType(), coupon.getDiscountType(), coupon.getDiscountAmount(), coupon.getExpirationMonth());
    }
}
