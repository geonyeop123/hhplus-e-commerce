package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.userCoupon.UserCouponCommand;

public record CouponCriteria(
) {
    public record IssueUserCoupon(Long userId, Long couponId) {
        UserCouponCommand.Issue toCommand(User user, Coupon coupon){
            return new UserCouponCommand.Issue(user, coupon);
        }
    }

}
