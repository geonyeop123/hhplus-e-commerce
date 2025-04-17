package kr.hhplus.be.server.interfaces.order;

import kr.hhplus.be.server.application.order.OrderCriteria;

import java.util.List;

public record OrderRequest(
) {

    public record Create(
            Long userId,
            Long userCouponId,
            List<OrderItem> orderItems
    ){
        public OrderCriteria.Create toCriteria(){
            return new OrderCriteria.Create(
                    this.userId,
                    this.userCouponId,
                    this.orderItems.stream()
                        .map(OrderRequest.OrderItem::toCriteria)
                        .toList());
        }
    }

    public record OrderItem(
            Long productId,
            int quantity
    ) {
        OrderCriteria.Create.OrderItem toCriteria(){
            return new OrderCriteria.Create.OrderItem(productId, quantity);
        }
    }
}
