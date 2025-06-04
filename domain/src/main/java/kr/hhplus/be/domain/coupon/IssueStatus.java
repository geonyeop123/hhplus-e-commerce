package kr.hhplus.be.domain.coupon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IssueStatus {

    ING("발급 중"),
    FINISH("종료");

    private final String text;
}
