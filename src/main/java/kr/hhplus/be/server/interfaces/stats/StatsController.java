package kr.hhplus.be.server.interfaces.stats;

import kr.hhplus.be.server.interfaces.product.response.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StatsController implements StatsDocs{

    @GetMapping("/api/v1/stats/products/popular")
    public ResponseEntity<List<ProductResponse>> popularProduct(){
        return ResponseEntity.ok(List.of(
                new ProductResponse(1L, "맥북", 30, 10000)
                , new ProductResponse(2L, "맥북에어", 40, 5000)
                , new ProductResponse(3L, "맥북M1", 50, 20000)
                , new ProductResponse(4L, "맥북M2", 60, 30000)
                , new ProductResponse(5L, "맥북M3", 70, 40000)
        ));
    }
}
