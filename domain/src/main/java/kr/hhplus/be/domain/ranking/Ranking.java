package kr.hhplus.be.domain.ranking;

import java.util.List;


public record Ranking(
        RankingType type,
        List<SalesProduct> products
) {

}