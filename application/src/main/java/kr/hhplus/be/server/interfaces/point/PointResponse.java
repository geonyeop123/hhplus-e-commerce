package kr.hhplus.be.server.interfaces.point;


import kr.hhplus.be.domain.point.Point;

public record PointResponse (
        Long userId,
        int balance
){
    public static PointResponse from(Point point) {
        return new PointResponse (point.getUserId(), point.getBalance());
    }
}
