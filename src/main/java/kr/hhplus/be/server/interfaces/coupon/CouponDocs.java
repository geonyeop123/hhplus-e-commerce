package kr.hhplus.be.server.interfaces.coupon;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.interfaces.coupon.response.CouponResponse;
import kr.hhplus.be.server.interfaces.product.response.ProductResponse;
import kr.hhplus.be.server.support.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "coupon", description = "coupon API")
public interface CouponDocs {

    @Operation(summary = "선착순 쿠폰 발급", description = "선착순 쿠폰을 발급합니다." +
            "<br> 재고가 없는 경우 혹은 발급 시기가 아닌 경우 발급되지 않습니다.")
    @ApiResponses(value ={
            @ApiResponse(
                    responseCode = "200",
                    description = "쿠폰 발급 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CouponResponse.class),
                            examples = @ExampleObject(value = """
                    {
                      "userId": 1,
                      "userCouponId": 1,
                      "discountAmount": 100000,
                      "expirationAt": "2024-05-05",
                      "usedAt" : null
                            }
                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 쿠폰 요청, 잘못된 사용자 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                    {
                      "code": 400,
                      "message": "조회된 쿠폰이 없습니다."
                    }
                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "재고 부족",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                    {
                      "code": 409,
                      "message": "쿠폰이 모두 소진되었습니다."
                    }
                    """)
                    )
            )
    })
    public ResponseEntity<CouponResponse> issue(@PathVariable Long userId, @PathVariable Long couponId);

    @Operation(summary = "사용자 보유 쿠폰 조회", description = "요청한 사용자의 보유 쿠폰을 조회합니다.")
    @ApiResponses(value ={
            @ApiResponse(
                    responseCode = "200",
                    description = "쿠폰 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductResponse.class),
                            examples = @ExampleObject(value = """
                    [{
                      "userId": 1,
                      "userCouponId": 1,
                      "name": "4월 깜짝 할인 쿠폰",
                      "discountAmount": 100000,
                      "expirationAt": "2024-05-05",
                      "usedAt" : null
                            },
                     {
                      "userId": 1,
                      "userCouponId": 2,
                      "name": "3월 깜짝 할인 쿠폰",
                      "discountAmount": 200000,
                      "expirationAt": "2024-04-05",
                      "usedAt" : null
                            }]
                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 사용자 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                    {
                      "code": 400,
                      "message": "조회된 쿠폰이 없습니다."
                    }
                    """)
                    )
            )
    })
    public ResponseEntity<List<CouponResponse>> coupons(@PathVariable Long userId);
}
