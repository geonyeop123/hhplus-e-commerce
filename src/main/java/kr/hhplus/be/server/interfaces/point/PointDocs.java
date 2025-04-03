package kr.hhplus.be.server.interfaces.point;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.interfaces.point.request.PointChargeRequest;
import kr.hhplus.be.server.interfaces.point.response.PointResponse;
import kr.hhplus.be.server.support.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "point", description = "point API")
public interface PointDocs {

    @Operation(summary = "포인트 조회", description = "userId의 해당하는 사용자의 포인트를 조회합니다." +
            "<br> 조회된 사용자가 없을 경우 포인트 조회에 실패합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "포인트 조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PointResponse.class),
                    examples = @ExampleObject(value = """
                {
                  "userId": 1,
                  "balance": 50000
                }
                """)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 사용자 요청",
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
    ResponseEntity<PointResponse> point(@Parameter(description = "포인트 조회 요청 정보", required = true)
                                        @PathVariable Long userId);

    @Operation(summary = "포인트 충전", description = "userId의 해당하는 사용자의 포인트를 충전합니다." +
            "<br>충전하는 포인트는 0이상만 충전이 가능하며, 충전 이후 최대 잔고는 10,000,000원을 넘을 수 없습니다."
        )
    @ApiResponses(value ={
        @ApiResponse(
            responseCode = "200",
            description = "포인트 충전 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PointResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "userId": 1,
                      "balance": 50000
                            }
                    """)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 사용자 요청, 충전 금액 부족, 최대 잔고 초과",
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
    ResponseEntity<PointResponse> charge(
            @PathVariable Long userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "충전 요청 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PointChargeRequest.class),
                            examples = @ExampleObject(value = """
                    {
                      "amount": 10000
                    }
                    """)
                    )
            )
            @RequestBody PointChargeRequest request);
}
