package kr.hhplus.be.infra.stats;

public interface NativePopularProduct {
    Long getProductId();
    Long getTotalQuantity();
    String getName();
    int getPrice();
    int getStock();
}
