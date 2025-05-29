package kr.hhplus.be.server.interfaces.order;

import kr.hhplus.be.domain.user.User;
import kr.hhplus.be.server.application.order.OrderCriteria;

import java.util.List;

public record OrderRequest(
) {

    public record Create(
            Long userCouponId,
            List<OrderItem> orderItems
    ){
        public OrderCriteria.Create toCriteria(User user){
            return new OrderCriteria.Create(
                    user,
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
