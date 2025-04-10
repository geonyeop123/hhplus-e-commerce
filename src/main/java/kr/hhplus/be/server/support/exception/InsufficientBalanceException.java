package kr.hhplus.be.server.support.exception;

import org.springframework.http.HttpStatus;

public class InsufficientBalanceException extends BusinessException {
    public InsufficientBalanceException() {
        super("보유 포인트가 부족합니다.", HttpStatus.BAD_REQUEST);
    }
}
