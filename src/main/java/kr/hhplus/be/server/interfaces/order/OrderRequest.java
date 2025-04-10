package kr.hhplus.be.server.interfaces.order;

import kr.hhplus.be.server.application.order.OrderCriteria;

import java.util.List;

public record OrderRequest(
) {

    public record Create(
            Long userId,
            List<OrderItem> orderItems
    ){
        public OrderCriteria.Create toCriteria(){
            return new OrderCriteria.Create(
                    this.userId,
                    this.orderItems.stream()
                        .map(OrderItem::toCriteria)
                        .toList());
        }
    }

    public record OrderItem(
            Long productId,
            int quantity
    ) {
        OrderCriteria.Create.OrderLine toCriteria(){
            return new OrderCriteria.Create.OrderLine(productId, quantity);
        }
    }
}
