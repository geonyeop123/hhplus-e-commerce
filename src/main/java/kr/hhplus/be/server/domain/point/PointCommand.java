package kr.hhplus.be.server.domain.point;

public record PointCommand(
) {
    public record Charge(
            Long userId,
            int amount
    ){

    }
    public record Use(
            Long userId,
            int amount
    ){

    }
}
