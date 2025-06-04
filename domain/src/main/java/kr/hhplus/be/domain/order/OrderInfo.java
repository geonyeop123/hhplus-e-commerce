package kr.hhplus.be.domain.order;

import java.time.LocalDateTime;
import java.util.List;

public record OrderInfo(
        Long id,
        Long userId,
        List<OrderProduct> orderProducts,
        OrderStatus status,
        Long userCouponId,
        int orderAmount,
        int finalAmount,
        LocalDateTime orderDateTime
) {
    public static OrderInfo from(Order order) {
        return new OrderInfo(order.getId(), order.getUserId(), order.getOrderProducts(), order.getStatus()
        , order.getUserCouponId(), order.getOrderAmount(), order.getFinalAmount(), order.getOrderDateTime());
    }
}
