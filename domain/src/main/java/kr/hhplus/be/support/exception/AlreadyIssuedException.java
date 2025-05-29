package kr.hhplus.be.support.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlreadyIssuedException extends BusinessException {
    public AlreadyIssuedException(String message, Long userId, Long couponId) {
        super(message);
        log.error("AlreadyIssuedException, userId : {}, couponId : {}", userId, couponId);
    }
}
