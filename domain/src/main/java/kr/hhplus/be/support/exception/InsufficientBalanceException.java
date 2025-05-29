package kr.hhplus.be.support.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InsufficientBalanceException extends BusinessException {
    public InsufficientBalanceException(int balance, int amount) {
        super("보유 포인트가 부족합니다.");
        log.error("InsufficientBalanceException, have balance : {}, request use amount : {}", balance, amount);
    }
}
