package kr.hhplus.be.support.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotEnoughStockException extends BusinessException {
    public NotEnoughStockException(int currentStock, int requiredStock) {
        super("재고가 부족합니다.");
        log.error("NotEnoughStockException, currentStock : {}, requiredStock : {}", currentStock, requiredStock);
    }
}
