package kr.hhplus.be.domain.coupon;

import java.time.LocalDate;

public record CouponCommand(

) {
    public record Register(
            String name, CouponType type,
            DiscountType discountType, int discountAmount,
            int expirationMonth, LocalDate issueStartDate, LocalDate issueEndDate, int initialQuantity) {

    }
}
