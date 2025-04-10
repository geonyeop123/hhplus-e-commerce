package kr.hhplus.be.server.interfaces.product;

import kr.hhplus.be.server.domain.product.ProductCommand;

public record ProductRequest() {

    public record Products(int pageNo, int pageSize) {
        public ProductCommand toCommand() {
            return new ProductCommand(pageNo, pageSize);
        }
    }
}
