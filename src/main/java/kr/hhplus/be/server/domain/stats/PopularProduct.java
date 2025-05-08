package kr.hhplus.be.server.domain.stats;

public record PopularProduct(
        Long productId,
        Long totalQuantity,
        String name,
        int price,
        int stock
) {

}
