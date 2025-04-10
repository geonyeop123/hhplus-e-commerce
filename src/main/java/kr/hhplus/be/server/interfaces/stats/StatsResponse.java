package kr.hhplus.be.server.interfaces.stats;

import kr.hhplus.be.server.interfaces.product.ProductResponse;

import java.util.List;

public record StatsResponse(

) {
    public record PopularSalesCount(
            List<ProductResponse> products
    ){

    }
}
