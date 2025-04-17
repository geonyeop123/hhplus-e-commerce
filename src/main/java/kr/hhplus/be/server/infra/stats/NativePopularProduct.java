package kr.hhplus.be.server.infra.stats;

public interface NativePopularProduct {
    Long getProductId();
    Long getTotalQuantity();
    String getName();
    int getPrice();
    int getStock();
}
