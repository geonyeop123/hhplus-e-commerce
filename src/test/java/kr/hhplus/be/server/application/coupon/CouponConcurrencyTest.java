package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.domain.coupon.DiscountType;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.infra.coupon.JpaCouponRepository;
import kr.hhplus.be.server.infra.user.JpaUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CouponConcurrencyTest {

    @Autowired
    private CouponFacade couponFacade;

    @Autowired
    private JpaCouponRepository jpaCouponRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @DisplayName("동일한 쿠폰에 대한 발급을 요청한 경우 발급된 개수만큼 쿠폰의 잔여 개수가 차감된다.")
    @Test
    void issueByCoupon() throws InterruptedException {
        // given
        Coupon coupon = Coupon.create("쿠폰", CouponType.TOTAL, DiscountType.FIXED, 1000, 3, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), 10);
        jpaCouponRepository.save(coupon);

        int threadCount = 13;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successCnt = new AtomicInteger();

        for (int i = 1; i <= threadCount; i++) {
            User user = User.create("user" + i);
            jpaUserRepository.save(user);
        }

        Long couponId = coupon.getId();

        // when
        for (long i = 1; i <= threadCount; i++) {
            long userId = i;
            executorService.submit(() -> {
                try {
                    User user = jpaUserRepository.findById(userId).orElseThrow();
                    CouponCriteria.IssueUserCoupon criteria = new CouponCriteria.IssueUserCoupon(user, couponId);
                    couponFacade.issueUserCoupon(criteria);
                    successCnt.getAndIncrement();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 작업이 끝날 때까지 대기

        // then
        assertThat(successCnt.get()).isNotZero();
        Coupon findCoupon = jpaCouponRepository.findById(couponId).orElseThrow();
        assertThat(findCoupon.getQuantity()).isEqualTo(coupon.getInitialQuantity() - successCnt.get());
    }

}