package kr.hhplus.be.support.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MaximumBalanceException extends BusinessException {
    public MaximumBalanceException(int balance) {
        super("충전 이후 포인트는 10,000,000포인트를 넘을 수 없습니다.");
        log.error("MaximumBalanceException after point : {}", balance);
    }
}
