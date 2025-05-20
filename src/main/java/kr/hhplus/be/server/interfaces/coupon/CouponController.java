package kr.hhplus.be.server.interfaces.coupon;

import kr.hhplus.be.server.application.coupon.CouponCriteria;
import kr.hhplus.be.server.application.coupon.CouponFacade;
import kr.hhplus.be.server.domain.common.PageResult;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.userCoupon.UserCoupon;
import kr.hhplus.be.server.domain.userCoupon.UserCouponService;
import kr.hhplus.be.server.interfaces.common.CurrentUser;
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

    private final CouponFacade couponFacade;
    private final CouponService couponService;
    private final UserCouponService userCouponService;

    @PostMapping("/api/v1/coupons/{couponId}")
    @Deprecated(since = "2025-05-20", forRemoval = true)
    public ResponseEntity<CouponResponse> issue(@CurrentUser User user, @PathVariable Long couponId) {
        CouponCriteria.IssueUserCoupon criteria = new CouponCriteria.IssueUserCoupon(user, couponId);
        return ResponseEntity.ok(CouponResponse.from(couponFacade.issueUserCoupon(criteria)));
    }

    @GetMapping("/api/v1/coupons")
    public ResponseEntity<PageResponse<CouponResponse>> coupons(
            @CurrentUser User user
            , CouponRequest.Coupons request
    ) {

        PageResult<UserCoupon> result = userCouponService.findAllByUserId(request.toCommand(user));

        PageResponse<CouponResponse> response = new PageResponse<>(
                result.content().stream().map(CouponResponse::from).toList()
                , result.page(), result.size(), result.totalCount(), result.totalPages());

        return ResponseEntity.ok().body(response);
    }
}
