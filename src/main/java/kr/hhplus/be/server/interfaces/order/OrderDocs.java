package kr.hhplus.be.server.interfaces.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.interfaces.order.request.CreateOrderRequest;
import kr.hhplus.be.server.interfaces.order.response.OrderResponse;
import kr.hhplus.be.server.interfaces.point.request.PointChargeRequest;
import kr.hhplus.be.server.support.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "order", description = "order API")
public interface OrderDocs {

    @Operation(summary = "주문 및 결제", description = "userId와 주문 상품 목록을 받아 주문을 생성하고 결제를 진행합니다." +
            "<br>포인트가 부족할 시 결제가 실패합니다." +
            "<br>쿠폰을 사용하는 경우 유효하지 않은 쿠폰일 시 결제에 실패합니다."
    )
    @ApiResponses(value ={
            @ApiResponse(
                    responseCode = "200",
                    description = "주문 및 결제 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class),
                            examples = @ExampleObject(value = """
                    {
                      "orderId": 1,
                      "paymentId" : 1,
                      "totalOrderAmount" : 10000,
                      "totalPaymentAmount" : 10000
                    }
                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 사용자 요청, 포인트 잔고 부족, 잘못된 쿠폰 사용 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                    {
                      "code": 400,
                      "message": "조회된 유저가 없습니다."
                    }
                    """)
                    )
            )
    })
    public ResponseEntity<OrderResponse> order(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "주문 및 결제 요청 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PointChargeRequest.class),
                            examples = @ExampleObject(value = """
                    {
                      "userId": 1,
                      "userCouponId": 1,
                      "orderItems" : [
                        {"productId" : 1, "quantity" : 2},
                        {"productId" : 2, "quantity" : 2}
                        ]
                    }
                    """)
                    )
            )
            @RequestBody CreateOrderRequest request);
}
