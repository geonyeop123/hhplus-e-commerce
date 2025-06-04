package kr.hhplus.be.domain.coupon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DiscountType {
//    RATE("정률"),
    FIXED("정액")
    ;

    private final String text;
}
