package kr.hhplus.be.domain.ranking;

import kr.hhplus.be.domain.product.ProductInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SalesProduct {
    private Long productId;
    private String name;
    private int price;
    private int stock;
    private int sales;

    public SalesProduct(Long productId, String name, int price, int stock, int sales) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.sales = sales;
    }

    public static SalesProduct create(Long productId, int sales){
        return new SalesProduct(productId, null, 0, 0, sales);
    }

    public void setProductInfo(ProductInfo productInfo){
        this.name = productInfo.name();
        this.price = productInfo.price();
        this.stock = productInfo.stock();
    }
}
