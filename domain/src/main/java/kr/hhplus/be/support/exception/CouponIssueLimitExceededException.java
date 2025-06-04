package kr.hhplus.be.support.exception;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CouponIssueLimitExceededException extends BusinessException {
    public CouponIssueLimitExceededException(Long couponId) {
        super("발급 가능한 수량을 초과하였습니다.");
        log.error("CouponIssueLimitExceededException, couponId: {}", couponId);
    }
}
