package kr.hhplus.be.server.support.exception;

public record ErrorResponse(
        String code,
        String message
) {
}