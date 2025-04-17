package kr.hhplus.be.server.domain.order;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.hhplus.be.server.domain.product.ProductInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
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
