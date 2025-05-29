package kr.hhplus.be.server.interfaces.product;


import kr.hhplus.be.domain.product.ProductCommand;

public record ProductRequest() {

    public record Products(int pageNo, int pageSize) {
        public ProductCommand.FindAll toCommand() {
            return new ProductCommand.FindAll(pageNo, pageSize);
        }
    }
}
