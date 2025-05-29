package kr.hhplus.be.domain.product;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.hhplus.be.domain.common.BaseEntity;
import kr.hhplus.be.support.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor
public class Product extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int stock;
    private int price;

    public void validatePurchasable(int deductAmount){
        if(stock - deductAmount < 0){
            throw new NotEnoughStockException(this.stock, deductAmount);
        }
    }

    public void deductStock(int amount){
        if(stock - amount < 0){
            throw new NotEnoughStockException(this.stock, amount);
        }
        stock -= amount;
    }

    private Product(String name, int stock, int price) {
        this.name = name;
        this.stock = stock;
        this.price = price;
    }

    public static Product create(String name, int stock, int price){
        return new Product(name, stock, price);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
