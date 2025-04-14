package kr.hhplus.be.server.domain.coupon;

import java.util.Optional;

public interface CouponRepository {
    Optional<Coupon> findById(Long aLong);
    Coupon save(Coupon coupon);
    void deleteAllInBatch();
}
