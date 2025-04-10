package kr.hhplus.be.server.domain.userCoupon;

import java.util.List;
import java.util.Optional;

public interface UserCouponRepository {
    UserCoupon save(UserCoupon userCoupon);
    long findCountByUserId(Long userId);
    List<UserCoupon> findAllByUserId(UserCouponCommand.FindAll command);

    Optional<UserCoupon> findById(Long id);
}
