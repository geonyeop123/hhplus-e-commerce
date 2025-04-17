package kr.hhplus.be.server.domain.userCoupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.user.User;

public record UserCouponCommand(

) {
    public record FindAll(
            Long userId, int pageNo, int pageSize
    ){

    }

    public record Issue(
            User user, Coupon coupon
    ) {

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
