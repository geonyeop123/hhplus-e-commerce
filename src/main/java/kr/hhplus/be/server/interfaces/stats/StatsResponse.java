package kr.hhplus.be.server.interfaces.stats;

import kr.hhplus.be.server.domain.stats.PopularProduct;

import java.util.List;

public record StatsResponse(

) {

    public record PopularProductResponse(
            List<ProductResponse> products
    ) {
        public static PopularProductResponse from(List<PopularProduct> products){
            return new PopularProductResponse(products.stream().map(ProductResponse::from).toList());
        }
    }
    public record ProductResponse(
            Long productId,
            Long totalQuantity,
            String name,
            int price,
            int stock
    ){
        private static ProductResponse from(PopularProduct product){
            return new ProductResponse(product.productId(), product.totalQuantity(), product.name(), product.price(), product.stock());
        }
    }
}
