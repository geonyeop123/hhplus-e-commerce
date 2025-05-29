package kr.hhplus.be.infra.coupon;

import kr.hhplus.be.domain.coupon.Coupon;
import kr.hhplus.be.domain.coupon.CouponRepository;
import kr.hhplus.be.domain.coupon.IssueStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {

    private final JpaCouponRepository jpaCouponRepository;
    private final RedisCouponRepository redisCouponRepository;

    @Override
    public Coupon save(Coupon coupon) {
        return jpaCouponRepository.save(coupon);
    }

    @Override
    public Optional<Coupon> findById(Long id) {
        return jpaCouponRepository.findById(id);
    }

    @Override
    public Optional<Coupon> findByIdForUpdate(Long id) {
        return jpaCouponRepository.findByIdForUpdate(id);
    }

    @Override
    public List<Coupon> findByIssueStatusIsIng() {
        return jpaCouponRepository.findByIssueStatusIs(IssueStatus.ING);
    }

}
