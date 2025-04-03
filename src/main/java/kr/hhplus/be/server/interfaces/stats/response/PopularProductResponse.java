package kr.hhplus.be.server.interfaces.stats.response;

import kr.hhplus.be.server.interfaces.product.response.ProductResponse;

import java.util.List;

public record PopularProductResponse(
        List<ProductResponse> products
) {
}
