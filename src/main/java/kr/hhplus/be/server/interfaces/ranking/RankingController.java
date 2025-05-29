package kr.hhplus.be.server.interfaces.ranking;

import kr.hhplus.be.domain.ranking.Ranking;
import kr.hhplus.be.server.application.ranking.RankingFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RankingController implements RankDocs{

    private final RankingFacade rankingFacade;

    @GetMapping("/api/v1/ranking/daily")
    public ResponseEntity<List<RankingResponse>> dailyRanking(){
        Ranking dailyRankingProducts = rankingFacade.findDailyRankingProducts();
        List<RankingResponse> list = dailyRankingProducts.products().stream().map(RankingResponse::from).toList();
        return ResponseEntity.ok(list);
    }
}
