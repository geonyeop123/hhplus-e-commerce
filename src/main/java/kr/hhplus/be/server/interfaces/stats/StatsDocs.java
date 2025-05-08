package kr.hhplus.be.server.interfaces.stats;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.interfaces.product.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;

@Tag(name = "stats", description = "stats API")
public interface StatsDocs {

    @Operation(summary = "인기 상품 조회", description = "3일간 가장 판매가 많은 순으로 5개의 상품이 조회됩니다.")
    @ApiResponses(value ={
            @ApiResponse(
                    responseCode = "200",
                    description = "인기 상품 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductResponse.class),
                            examples = @ExampleObject(value = """
                    {
                      "products" : [
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
    public ResponseEntity<StatsResponse.PopularProductResponse> popularProduct(@ModelAttribute StatsRequest request);
}
