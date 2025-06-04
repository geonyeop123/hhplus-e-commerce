package kr.hhplus.be.domain.product;

public record ProductInfo(
        Long id,
        String name,
        int price,
        int stock
) {
    public static ProductInfo from(Product product) {
        return new ProductInfo(product.getId(), product.getName(), product.getPrice(), product.getStock());
    }
}
