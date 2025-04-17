package kr.hhplus.be.server.support.exception;

import org.springframework.http.HttpStatus;

public class CouponIssueLimitExceededException extends BusinessException {
    public CouponIssueLimitExceededException() {
        super("발급 가능한 수량을 초과하였습니다.", HttpStatus.BAD_REQUEST);
    }
}
