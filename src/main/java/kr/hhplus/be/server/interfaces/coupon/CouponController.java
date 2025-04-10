package kr.hhplus.be.server.interfaces.coupon;

import kr.hhplus.be.server.domain.common.PageResult;
import kr.hhplus.be.server.domain.coupon.CouponCommand;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.userCoupon.UserCoupon;
import kr.hhplus.be.server.domain.userCoupon.UserCouponService;
import kr.hhplus.be.server.interfaces.common.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CouponController implements CouponDocs {

    private final CouponService couponService;
    private final UserCouponService userCouponService;

    @PostMapping("/api/v1/users/{userId}/coupons/{couponId}")
    public ResponseEntity<CouponResponse> issue(@PathVariable Long userId, @PathVariable Long couponId) {
        CouponCommand couponCommand = new CouponCommand(userId, couponId);
        return ResponseEntity.ok(CouponResponse.from(couponService.issue(couponCommand)));
    }

    @GetMapping("/api/v1/users/{userId}/coupons")
    public ResponseEntity<PageResponse<CouponResponse>> coupons(
            @PathVariable Long userId
            , CouponRequest.Coupons request
    ) {

        PageResult<UserCoupon> result = userCouponService.findAllByUserId(request.toCommand(userId));

        PageResponse<CouponResponse> response = new PageResponse<>(
                result.content().stream().map(CouponResponse::from).toList()
                , result.page(), result.size(), result.totalCount(), result.totalPages());

        return ResponseEntity.ok().body(response);
    }
}
