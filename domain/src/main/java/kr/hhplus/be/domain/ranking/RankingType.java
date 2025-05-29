package kr.hhplus.be.domain.ranking;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RankingType {
    DAILY("일간"),
    WEEKLY("주간"),
    MONTHLY("월간");

    private final String text;
}
