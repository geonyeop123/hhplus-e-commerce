package kr.hhplus.be.domain.coupon;

import java.util.List;
import java.util.Optional;

public interface CouponRepository {
    Coupon save(Coupon coupon);
    Optional<Coupon> findById(Long id);
    Optional<Coupon> findByIdForUpdate(Long id);
    List<Coupon> findByIssueStatusIsIng();

    boolean issueCall(Long couponId, Long userId);
}
