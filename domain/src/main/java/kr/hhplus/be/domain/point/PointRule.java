package kr.hhplus.be.domain.point;

import lombok.Getter;

@Getter
public enum PointRule {
    CHARGE(0, 10_000_000);

    private final int min;
    private final int max;

    PointRule(int min, int max) {
        this.min = min;
        this.max = max;
    }
}
