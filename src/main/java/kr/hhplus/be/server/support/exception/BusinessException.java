package kr.hhplus.be.server.support.exception;

import org.springframework.http.HttpStatus;

public abstract class BusinessException extends RuntimeException{
    private final HttpStatus status;

    protected BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
