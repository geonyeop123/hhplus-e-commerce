package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.product.Product;
import lombok.Getter;

@Getter
public class OrderProduct {
    private Long id;
    private Product product;
    private int price;
    private int quantity;

    public OrderProduct(Product product, int quantity) {
        this.product = product;
        this.price = product.getPrice();
        this.quantity = quantity;
    }

    public static OrderProduct create(Product product, int quantity){
        return new OrderProduct(product, quantity);
    }
}
