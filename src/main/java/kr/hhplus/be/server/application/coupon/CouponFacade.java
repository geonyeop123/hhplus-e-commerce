package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.domain.userCoupon.UserCoupon;
import kr.hhplus.be.server.domain.userCoupon.UserCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponFacade {

    private final UserService userService;
    private final CouponService couponService;
    private final UserCouponService userCouponService;

    public CouponResult.IssueUserCoupon issueUserCoupon(CouponCriteria.IssueUserCoupon criteria) {

        User user = userService.findById(criteria.userId());
        Coupon coupon = couponService.findById(criteria.couponId());
        UserCoupon userCoupon = userCouponService.issue(criteria.toCommand(user, coupon));

        return CouponResult.IssueUserCoupon.from(userCoupon);
    }

}
