package kr.hhplus.be.support.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExpiredException extends BusinessException {
    public ExpiredException(Long userCouponId) {
        super("유효기간이 만료된 쿠폰입니다.");
        log.error("ExpiredException, userCouponId : {}", userCouponId);
    }
}
