package kr.hhplus.be.server.infra.userCoupon;

import kr.hhplus.be.server.domain.userCoupon.UserCoupon;
import kr.hhplus.be.server.domain.userCoupon.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserCouponRepositoryImpl implements UserCouponRepository {

    private final JpaUserCouponRepository jpaUserCouponRepository;

    @Override
    public UserCoupon save(UserCoupon userCoupon) {
        return jpaUserCouponRepository.save(userCoupon);
    }

    @Override
    public Page<UserCoupon> findAllByUserId(Long userId, Pageable pageable) {
        return jpaUserCouponRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public Optional<UserCoupon> findById(Long id) {
        return jpaUserCouponRepository.findById(id);
    }
}
