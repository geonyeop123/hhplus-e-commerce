package kr.hhplus.be.server.domain.order;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.product.ProductInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "order_product",
        indexes = {

        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private String name;

    private int price;

    private int quantity;

    private OrderProduct(ProductInfo productInfo, int quantity) {
        this.productId = productInfo.id();
        this.name = productInfo.name();
        this.price = productInfo.price();
        this.quantity = quantity;
    }

    public static OrderProduct create(ProductInfo productInfo, int quantity){
        return new OrderProduct(productInfo, quantity);
    }
}
