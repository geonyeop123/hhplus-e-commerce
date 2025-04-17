package kr.hhplus.be.server.support.exception;

import org.springframework.http.HttpStatus;

public class CouponIssuePeriodException extends BusinessException {
    public CouponIssuePeriodException() {
        super("쿠폰 발급 기간이 아닙니다.", HttpStatus.BAD_REQUEST);
    }
}
