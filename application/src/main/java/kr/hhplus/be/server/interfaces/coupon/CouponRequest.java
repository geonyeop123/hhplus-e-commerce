package kr.hhplus.be.server.interfaces.coupon;


import kr.hhplus.be.domain.user.User;
import kr.hhplus.be.domain.userCoupon.UserCouponCommand;

public record CouponRequest(

) {
    public record Coupons(
            int pageNo, int pageSize
    ) {
        UserCouponCommand.FindAll toCommand(User user){
            return new UserCouponCommand.FindAll(user, this.pageNo, this.pageSize);
        }
    }
}
