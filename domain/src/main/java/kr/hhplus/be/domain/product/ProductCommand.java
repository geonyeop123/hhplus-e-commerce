package kr.hhplus.be.domain.product;

public record ProductCommand(
) {
    public record FindAll(int pageNo, int pageSize) {
    }

    public record ValidatePurchase(Long productId, int quantity) {

    }

    public record DeductStock(Long productId, int quantity) {

    }
}
