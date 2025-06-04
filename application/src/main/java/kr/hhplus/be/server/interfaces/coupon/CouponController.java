package kr.hhplus.be.server.interfaces.coupon;

import kr.hhplus.be.domain.common.PageResult;
import kr.hhplus.be.domain.coupon.CouponCommand;
import kr.hhplus.be.domain.coupon.CouponService;
import kr.hhplus.be.domain.user.User;
import kr.hhplus.be.domain.userCoupon.UserCoupon;
import kr.hhplus.be.domain.userCoupon.UserCouponService;
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

    private final CouponService couponService;
    private final UserCouponService userCouponService;

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

    @PostMapping("/api/v1/coupons/call/{couponId}")
    public ResponseEntity<Void> callIssue(@CurrentUser User user, @PathVariable Long couponId) {
        CouponCommand.IssueCall command = new CouponCommand.IssueCall(user, couponId);
        couponService.issueCall(command);
        return ResponseEntity.ok().build();
    }
}
