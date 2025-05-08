package kr.hhplus.be.server.interfaces.stats;

import kr.hhplus.be.server.domain.stats.PopularProducts;
import kr.hhplus.be.server.domain.stats.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StatsController implements StatsDocs{

    private final StatsService statsService;

    @GetMapping("/api/v1/stats/products/popular")
    public ResponseEntity<StatsResponse.PopularProductResponse> popularProduct(@ModelAttribute StatsRequest request){

        PopularProducts result = statsService.getPopularProducts(request.toCommand());
        return ResponseEntity.ok(StatsResponse.PopularProductResponse.from(result.getProducts()));
    }
}
