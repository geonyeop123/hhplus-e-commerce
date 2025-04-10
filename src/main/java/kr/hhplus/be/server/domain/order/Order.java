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
    private DiscountInfo discountInfo;
    private int orderAmount;
    private int finalAmount;
    private LocalDateTime orderDateTime;

    public void addOrderProduct(OrderProduct orderProduct) {
        orderProducts.add(orderProduct);
        orderProduct.setOrder(this);
    }

    public void calculateTotalAmount() {
        orderProducts.forEach(orderProduct ->
                orderAmount += orderProduct.getPrice() * orderProduct.getQuantity());
        int discountAmount = orderAmount - discountInfo.discountAmount();
        finalAmount = Math.max(discountAmount, 0);
    }

    public void complete(){
        this.status = OrderStatus.SUCCESS;
    }

    public static Order create(User user, DiscountInfo discountInfo){
        return new Order(user, OrderStatus.PENDING, discountInfo, LocalDateTime.now());
    }

    private Order(User user, OrderStatus status, DiscountInfo discountInfo, LocalDateTime orderDateTime) {
        this.user = user;
        this.status = status;
        this.discountInfo = discountInfo;
        this.orderDateTime = orderDateTime;
    }
}
