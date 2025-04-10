package kr.hhplus.be.server.interfaces.coupon;

import kr.hhplus.be.server.domain.userCoupon.UserCouponCommand;

public record CouponRequest(

) {
    public record Coupons(
            int pageNo, int pageSize
    ) {
        UserCouponCommand.FindAll toCommand(Long userId){
            return new UserCouponCommand.FindAll(userId, this.pageNo, this.pageSize);
        }
    }
}
