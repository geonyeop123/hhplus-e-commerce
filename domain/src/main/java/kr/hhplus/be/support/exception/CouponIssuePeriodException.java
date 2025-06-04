package kr.hhplus.be.support.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CouponIssuePeriodException extends BusinessException {
    public CouponIssuePeriodException(Long couponId) {
        super("쿠폰 발급 기간이 아닙니다.");
        log.error("CouponIssuePeriodException, couponId : {}", couponId);
    }
}
