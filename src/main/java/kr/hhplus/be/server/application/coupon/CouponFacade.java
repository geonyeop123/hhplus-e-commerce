package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.domain.userCoupon.UserCoupon;
import kr.hhplus.be.server.domain.userCoupon.UserCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponFacade {

    private final UserService userService;
    private final CouponService couponService;
    private final UserCouponService userCouponService;

    @Transactional
    public CouponResult.IssueUserCoupon issueUserCoupon(CouponCriteria.IssueUserCoupon criteria) {

//        User user = userService.findById(criteria.userId());
        Coupon coupon = couponService.issueValidate(criteria.couponId());
        UserCoupon userCoupon = userCouponService.issue(criteria.toCommand(coupon));
        couponService.deduct(criteria.couponId());

        return CouponResult.IssueUserCoupon.from(userCoupon);
    }
}
