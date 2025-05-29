package kr.hhplus.be.application.coupon;

import kr.hhplus.be.domain.coupon.Coupon;
import kr.hhplus.be.domain.coupon.CouponService;
import kr.hhplus.be.domain.user.User;
import kr.hhplus.be.domain.user.UserService;
import kr.hhplus.be.domain.userCoupon.UserCouponCommand;
import kr.hhplus.be.domain.userCoupon.UserCouponService;
import kr.hhplus.be.support.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponFacade {

    private final CouponService couponService;
    private final UserCouponService userCouponService;
    private final UserService userService;

    @Transactional
    public void issueUserCoupon(CouponCriteria.IssueUserCoupon criteria) {
        try{
            User user = userService.findById(criteria.userId());
            Coupon coupon = couponService.issueValidate(criteria.couponId());
            UserCouponCommand.Issue command = new UserCouponCommand.Issue(user, coupon);
            userCouponService.issue(command);
            couponService.deduct(criteria.couponId());
        }catch(BusinessException e){
            log.error("쿠폰 발급에 실패했습니다.");
        }
    }

}
