package kr.hhplus.be.server.interfaces.coupon;

import kr.hhplus.be.server.interfaces.coupon.response.CouponResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class CouponController implements CouponDocs {

    @PostMapping("/api/v1/users/{userId}/coupons/{couponId}")
    public ResponseEntity<CouponResponse> issue(@PathVariable Long userId, @PathVariable Long couponId) {
        return ResponseEntity.ok(new CouponResponse(1L, 1L, "4월 깜짝 할인 쿠폰", 10000, LocalDate.of(2025,5,4), null));
    }

    @GetMapping("/api/v1/users/{userId}/coupons")
    public ResponseEntity<List<CouponResponse>> coupons(@PathVariable Long userId) {
        List<CouponResponse> result = List.of(new CouponResponse(1L, 1L, "4월 깜짝 할인 쿠폰", 10000, LocalDate.of(2025,5,4), null));
         return ResponseEntity.ok().body(result);
    }
}
