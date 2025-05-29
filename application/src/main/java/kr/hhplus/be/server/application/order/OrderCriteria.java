package kr.hhplus.be.server.application.order;

import kr.hhplus.be.domain.product.ProductCommand;
import kr.hhplus.be.domain.user.User;

import java.util.List;
import java.util.stream.Collectors;

public record OrderCriteria(

) {
    public record Create(
            User user,
            Long userCouponId,
            List<OrderItem> orderItems
    ) {

        public List<Long> toLockKeys() {
            return orderItems.stream()
                    .map(OrderItem::productId)
                    .sorted()
                    .collect(Collectors.toList());
        }

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
