package kr.hhplus.be.server.domain.coupon;

public interface CouponRepository {
    Coupon findById(Long aLong);
    Coupon save(Coupon coupon);
}
