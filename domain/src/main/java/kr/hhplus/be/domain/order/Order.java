package kr.hhplus.be.domain.order;

import jakarta.persistence.*;
import kr.hhplus.be.domain.common.BaseEntity;
import kr.hhplus.be.domain.user.User;
import kr.hhplus.be.domain.userCoupon.UserCouponInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Table(
        name = "orders",
        indexes = {
                @Index(name = "idx_order_date_time_status", columnList = "order_date_time,status")
        }
)
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private final List<OrderProduct> orderProducts = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Long userCouponId;

    private int orderAmount;

    private int finalAmount;

    private LocalDateTime orderDateTime;

    public void addOrderProduct(OrderProduct orderProduct) {
        orderProducts.add(orderProduct);
        orderAmount += orderProduct.getPrice() * orderProduct.getQuantity();
        finalAmount = orderAmount;
    }


    public void complete(){
        this.status = OrderStatus.SUCCESS;
    }

    public static Order create(User user){
        return new Order(user, OrderStatus.PENDING, LocalDateTime.now());
    }

    private Order(User user, OrderStatus status, LocalDateTime orderDateTime) {
        this.userId = user.getId();
        this.status = status;
        this.orderDateTime = orderDateTime;
    }

    public void applyCoupon(UserCouponInfo userCouponInfo) {
        finalAmount -= userCouponInfo.discountAmount();
        finalAmount = Math.max(finalAmount, 0);
        this.userCouponId = userCouponInfo.id();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
