package kr.hhplus.be.server.domain.product;

public record ProductInfo(
        Long id,
        String name,
        int price
) {
    public static ProductInfo from(Product product) {
        return new ProductInfo(product.getId(), product.getName(), product.getPrice());
    }
}
