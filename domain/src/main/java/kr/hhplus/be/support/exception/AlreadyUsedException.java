package kr.hhplus.be.support.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlreadyUsedException extends BusinessException {
    public AlreadyUsedException(Long userCouponId) {
        super("이미 사용된 쿠폰입니다.");
        log.error("AlreadyUsedException, userCouponId : {}", userCouponId);
    }
}
