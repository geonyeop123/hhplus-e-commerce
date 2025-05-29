package kr.hhplus.be.infra.coupon;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.domain.coupon.Coupon;
import kr.hhplus.be.domain.coupon.IssueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JpaCouponRepository extends JpaRepository<Coupon, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Coupon c WHERE c.id = :id")
    Optional<Coupon> findByIdForUpdate(Long id);

    List<Coupon> findByIssueStatusIs(IssueStatus issueStatus);
}
