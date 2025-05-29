package kr.hhplus.be.infra.userCoupon;

import kr.hhplus.be.domain.userCoupon.UserCoupon;
import kr.hhplus.be.domain.userCoupon.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserCouponRepositoryImpl implements UserCouponRepository {

    private final JpaUserCouponRepository jpaUserCouponRepository;
    private final RedisUserCouponRepository redisUserCouponRepository;

    @Override
    public UserCoupon save(UserCoupon userCoupon) {
        issueChecked(userCoupon.getUserId(), userCoupon.getCouponId());
        return jpaUserCouponRepository.save(userCoupon);
    }

    @Override
    public void issueChecked(Long userId, Long couponId) {
        redisUserCouponRepository.issueChecked(userId, couponId);
    }

    @Override
    public boolean callIssue(Long userId, Long couponId) {
        return redisUserCouponRepository.callIssue(userId, couponId);
    }

    @Override
    public Page<UserCoupon> findAllByUserId(Long userId, Pageable pageable) {
        return jpaUserCouponRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public Optional<UserCoupon> findById(Long id) {
        return jpaUserCouponRepository.findById(id);
    }

    @Override
    public Optional<UserCoupon> findByUserIdAndCouponId(Long userId, Long couponId) {
        return jpaUserCouponRepository.findByUserIdAndCouponId(userId, couponId);
    }

    @Override
    public List<Long> findIssueTarget(Long couponId, int quantity) {
        return redisUserCouponRepository.findIssueTarget(couponId, quantity);
    }
}
