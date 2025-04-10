package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.user.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Order {
    private Long id;
    private User user;
    private final List<OrderProduct> orderProducts = new ArrayList<>();
    private OrderStatus status;
    private int totalOrderAmount;
    private int paymentAmount;
    private LocalDateTime orderDateTime;

    public void addOrderProduct(OrderProduct orderProduct) {
        orderProducts.add(orderProduct);
        orderProduct.setOrder(this);
    }

    public void calculateTotalAmount() {
        orderProducts.forEach(orderProduct ->
                totalOrderAmount += orderProduct.getPrice() * orderProduct.getQuantity());
        paymentAmount = totalOrderAmount;
    }

    public void complete(){
        this.status = OrderStatus.SUCCESS;
    }

    public static Order create(User user){
        return new Order(user, OrderStatus.PENDING, LocalDateTime.now());
    }

    private Order(User user, OrderStatus status, LocalDateTime orderDateTime) {
        this.user = user;
        this.status = status;
        this.orderDateTime = orderDateTime;
    }
}
