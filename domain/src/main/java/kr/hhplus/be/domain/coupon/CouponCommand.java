package kr.hhplus.be.domain.coupon;

import kr.hhplus.be.domain.user.User;

import java.time.LocalDate;

public record CouponCommand(

) {
    public record Register(
            String name, CouponType type,
            DiscountType discountType, int discountAmount,
            int expirationMonth, LocalDate issueStartDate, LocalDate issueEndDate, int initialQuantity) {
    }
    public record IssueCall(
            User user, Long couponId
    ){

    }
}
