package kr.hhplus.be.server.domain.userCoupon;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserCouponRepository {
    UserCoupon save(UserCoupon userCoupon);
    Page<UserCoupon> findAllByUserId(Long userId, Pageable pageable);
    Optional<UserCoupon> findById(Long id);
}
