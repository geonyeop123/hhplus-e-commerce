package kr.hhplus.be.server.interfaces.ranking;


import kr.hhplus.be.domain.ranking.SalesProduct;

public record RankingResponse(
        Long productId,
        String name,
        Integer stock,
        Integer price,
        Integer salesCount
) {
    public static RankingResponse from(SalesProduct salesProduct) {
        return new RankingResponse(salesProduct.getProductId(), salesProduct.getName(), salesProduct.getStock(), salesProduct.getPrice(), salesProduct.getSales());
    }
}
