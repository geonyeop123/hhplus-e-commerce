package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;

import java.util.List;

public record OrderCommand(
) {
    public record OrderLine(
            Product product,
            int quantity
    ) {
    }

    public record Create(
            User user,
            DiscountInfo discountInfo,
            List<OrderLine> orderLines
    ) {
    }
}
