package kr.hhplus.be.server.interfaces.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.interfaces.common.PageResponse;
import kr.hhplus.be.server.support.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "product", description = "product API")
public interface ProductDocs {

    @Operation(summary = "상품 조회", description = "productId에 해당하는 상품을 반환합니다.")
    @ApiResponses(value ={
            @ApiResponse(
                    responseCode = "200",
                    description = "상품 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductResponse.class),
                            examples = @ExampleObject(value = """
                    {
                      "productId": 1,
                      "name": "맥북",
                      "stock": 50,
                      "price": 100000
                            }
                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 상품 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                    {
                      "code": 400,
                      "message": "조회된 상품이 없습니다."
                    }
                    """)
                    )
            )
    })
    public ResponseEntity<ProductResponse> product(@PathVariable Long productId);

    @Operation(summary = "상품 목록 조회", description = "모든 상품 목록을 반환합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "상품 목록 조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductResponse.class),
                    examples = @ExampleObject(value = """
            [{
              "productId": 1,
              "name": "맥북",
              "stock": 50,
              "price": 100000
                    },
             {
              "productId": 2,
              "name": "맥북 에어",
              "stock": 50,
              "price": 50000
                    }]
            """)
            )
    )
    public ResponseEntity<PageResponse<ProductResponse>> products(ProductRequest.Products request);
}
