package kr.hhplus.be.domain.point;


import kr.hhplus.be.domain.user.User;

public record PointCommand(
) {
    public record Charge(
            User user,
            int amount
    ){

    }
    public record Use(
            User user,
            int amount
    ){

    }
}
