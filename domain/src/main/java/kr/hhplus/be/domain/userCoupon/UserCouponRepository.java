package kr.hhplus.be.domain.userCoupon;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserCouponRepository {
    UserCoupon save(UserCoupon userCoupon);
    void issueChecked(Long userId, Long couponId);
    boolean callIssue(Long userId, Long couponId);
    Page<UserCoupon> findAllByUserId(Long userId, Pageable pageable);
    Optional<UserCoupon> findById(Long id);
    Optional<UserCoupon> findByUserIdAndCouponId(Long userId, Long couponId);
    List<Long> findIssueTarget(Long couponId, int quantity);
}
