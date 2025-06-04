package kr.hhplus.be.domain.order;

import jakarta.persistence.*;
import kr.hhplus.be.domain.product.ProductInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "order_product",
        indexes = {
                @Index(name = "idx_order_id", columnList = "order_id"),
                @Index(name = "idx_product_id", columnList = "product_id")
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

    public ProductInfo getProductInfo() {
        return new ProductInfo(this.productId, this.name, this.price, 0);
    }
}
