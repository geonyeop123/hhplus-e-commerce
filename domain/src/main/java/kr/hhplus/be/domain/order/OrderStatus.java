package kr.hhplus.be.domain.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    PENDING("주문 대기"),
    SUCCESS("주문 완료"),
    FAILED("주문 실패");

    private final String text;
}
