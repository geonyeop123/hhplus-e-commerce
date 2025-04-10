package kr.hhplus.be.server.support.exception;

import org.springframework.http.HttpStatus;

public class ExpiredException extends BusinessException {
    public ExpiredException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
