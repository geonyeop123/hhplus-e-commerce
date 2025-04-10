package kr.hhplus.be.server.domain.point;

public record ChargeCommand(
        Long userId,
        int amount
) {

}
