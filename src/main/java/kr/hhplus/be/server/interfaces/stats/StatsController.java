package kr.hhplus.be.server.interfaces.stats;

import kr.hhplus.be.server.domain.stats.PopularProduct;
import kr.hhplus.be.server.domain.stats.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController implements StatsDocs{

    private final StatsService statsService;

    @GetMapping("/api/v1/stats/products/popular")
    public ResponseEntity<StatsResponse.PopularProductResponse> popularProduct(){
        List<PopularProduct> popularProducts = statsService.getPopularProducts();
        return ResponseEntity.ok(StatsResponse.PopularProductResponse.from(popularProducts));
    }
}
