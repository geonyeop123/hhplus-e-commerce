package kr.hhplus.be.server.interfaces.ranking;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "stats", description = "stats API")
public interface RankDocs {
    @Operation(summary = "일간 상품 순위 조회", description = "일간 판매된 상품이 순위순으로 조회됩니다.")
    @ApiResponses(value ={
            @ApiResponse(
                    responseCode = "200",
                    description = "일간 상품 순위 조회",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RankingResponse.class),
                            examples = @ExampleObject(value = """
                    {
                      [
                            {
                                "productId": 1,
                                "totalQuantity": "60",
                                "name": "맥북",
                                "stock": 50,
                                "price": 10000
                            },
                            {
                                "productId": 2,
                                "totalQuantity": "50",
                                "name": "맥북에어",
                                "stock": 40,
                                "price": 5000
                            },
                            {
                                "productId": 3,
                                "totalQuantity": "40",
                                "name": "맥북M1",
                                "stock": 50,
                                "price": 20000
                            },
                            {
                                "productId": 4,
                                "totalQuantity": "20",
                                "name": "맥북M2",
                                "stock": 60,
                                "price": 30000
                            },
                            {
                                "productId": 5,
                                "totalQuantity": "10",
                                "name": "맥북M3",
                                "stock": 70,
                                "price": 40000
                            }
                      ]
                    }
                    """)
                    )
            )
    })
    public ResponseEntity<List<RankingResponse>> dailyRanking();
}
