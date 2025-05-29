package kr.hhplus.be.domain.order;

public record OrderEvent(

) {
    public record Completed(
            OrderInfo orderInfo
    ) {

    }
}
