package kr.hhplus.be.domain.coupon;

import java.time.LocalDate;

public record CouponInfo(
        Long id,
        String name,
        CouponType type,
        DiscountType discountType,
        int discountAmount,
        LocalDate issueStartDate,
        LocalDate issueEndDate,
        int initialQuantity,
        int quantity
) {
    public static CouponInfo from(Coupon coupon) {
        return new CouponInfo(coupon.getId(), coupon.getName(), coupon.getType(), coupon.getDiscountType(), coupon.getDiscountAmount(), coupon.getIssueStartDate(), coupon.getIssueEndDate(), coupon.getInitialQuantity(), coupon.getQuantity());
    }
}
