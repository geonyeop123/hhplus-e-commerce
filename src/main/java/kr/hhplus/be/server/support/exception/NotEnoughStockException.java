package kr.hhplus.be.server.support.exception;

import org.springframework.http.HttpStatus;

public class NotEnoughStockException extends BusinessException {
    public NotEnoughStockException() {
        super("재고가 부족합니다.", HttpStatus.BAD_REQUEST);
    }
}
