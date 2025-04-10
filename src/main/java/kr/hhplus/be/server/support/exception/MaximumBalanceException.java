package kr.hhplus.be.server.support.exception;

import org.springframework.http.HttpStatus;

public class MaximumBalanceException extends BusinessException {
    public MaximumBalanceException() {
        super("충전 이후 포인트는 10,000,000포인트를 넘을 수 없습니다.", HttpStatus.BAD_REQUEST);
    }
}
