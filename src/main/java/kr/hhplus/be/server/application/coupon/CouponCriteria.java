package kr.hhplus.be.server.application.coupon;


import kr.hhplus.be.domain.coupon.Coupon;
import kr.hhplus.be.domain.user.User;
import kr.hhplus.be.domain.userCoupon.UserCouponCommand;

public record CouponCriteria(
) {
    public record IssueUserCoupon(User user, Long couponId) {
        UserCouponCommand.Issue toCommand(Coupon coupon){
            return new UserCouponCommand.Issue(user, coupon);
        }
    }

    public record Issue(
            Long couponId
    ) {
    }
}
