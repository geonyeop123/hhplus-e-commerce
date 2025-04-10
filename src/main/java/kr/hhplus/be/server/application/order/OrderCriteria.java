package kr.hhplus.be.server.application.order;

import java.util.List;

public record OrderCriteria(

) {
    public record Create(
            Long userId,
            List<OrderLine> orderLines
    ) {

        public record OrderLine(
                Long productId,
                int quantity
        ) {

        }
    }
}
