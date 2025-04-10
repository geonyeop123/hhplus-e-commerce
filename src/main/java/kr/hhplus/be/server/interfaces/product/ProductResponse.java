package kr.hhplus.be.server.interfaces.product;

import kr.hhplus.be.server.domain.product.Product;

public record ProductResponse(
        Long productId,
        String name,
        Integer stock,
        Integer price
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getStock(), product.getPrice());
    }
}
