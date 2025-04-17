package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.product.ProductCommand;

import java.util.List;

public record OrderCriteria(

) {
    public record Create(
            Long userId,
            Long userCouponId,
            List<OrderItem> orderItems
    ) {

        public void toProductCommand() {

        }

        public record OrderItem(
                Long productId,
                int quantity
        ) {
            public ProductCommand.ValidatePurchase toValidateCommand() {
                return new ProductCommand.ValidatePurchase(productId, quantity);
            }

            public ProductCommand.DeductStock toDeductCommand() {
                return new ProductCommand.DeductStock(productId, quantity);
            }
        }
    }
}
