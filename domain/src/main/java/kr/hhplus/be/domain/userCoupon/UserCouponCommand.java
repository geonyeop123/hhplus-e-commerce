package kr.hhplus.be.domain.userCoupon;


import kr.hhplus.be.domain.coupon.Coupon;

public record UserCouponCommand(

) {
    public record FindAll(
            User user, int pageNo, int pageSize
    ){

    }

    public record Issue(
            User user, Coupon coupon
    ) {

    }

    public record CallIssue(
        User user, Long couponId
    ){

    }

    public record FindIssueTarget(
            Long couponId,
            int quantity
    ){

    }

    public record Validate(
            Long userId, Long userCouponId
    ){
        public boolean isEmptyCoupon() {
            return null == userCouponId;
        }
    }

    public record Use(
            Long userId, Long userCouponId
    )
    {
        public boolean isEmptyCoupon() {
            return null == userCouponId;
        }
    }
}
