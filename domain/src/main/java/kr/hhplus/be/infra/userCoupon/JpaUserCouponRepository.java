package kr.hhplus.be.infra.userCoupon;

import kr.hhplus.be.domain.userCoupon.UserCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserCouponRepository extends JpaRepository<UserCoupon, Long> {
    Page<UserCoupon> findAllByUserId(Long userId, Pageable pageable);
    Optional<UserCoupon> findByUserIdAndCouponId(Long userId, Long couponId);
}
