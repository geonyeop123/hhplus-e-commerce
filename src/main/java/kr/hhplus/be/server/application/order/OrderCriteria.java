package kr.hhplus.be.server.application.order;

import java.util.List;

public record OrderCriteria(

) {
    public record Create(
            Long userId,
            Long userCouponId,
            List<OrderLine> orderLines
    ) {

        public boolean hasCoupon() {
            return null != userCouponId;
        }

        public record OrderLine(
                Long productId,
                int quantity
        ) {
        }
    }
}
