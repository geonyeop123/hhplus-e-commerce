package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.product.ProductInfo;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.userCoupon.UserCouponInfo;

import java.util.List;

public record OrderCommand(
) {
    public record OrderLine(
            ProductInfo product,
            int quantity
    ) {

    }

    public record Create(
            User user,
            UserCouponInfo userCouponInfo,
            List<OrderLine> orderLines
    ) {
    }
}
