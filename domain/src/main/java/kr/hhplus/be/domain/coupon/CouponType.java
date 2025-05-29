package kr.hhplus.be.domain.coupon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponType {
    TOTAL("주문 전체 금액")
//    , PRODUCT("상품")
    ;

    private final String text;
}
