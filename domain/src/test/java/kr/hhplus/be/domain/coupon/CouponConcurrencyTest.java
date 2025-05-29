package kr.hhplus.be.domain.coupon;

import kr.hhplus.be.infra.coupon.JpaCouponRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create"
})
public class CouponConcurrencyTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private JpaCouponRepository jpaCouponRepository;


    @DisplayName("쿠폰의 수량이 10개 남았을 때, 13명의 사용자가 을 동시에 수량 차감을 요청하면 남은 수량은 0이 된다.")
    @Test
    void deduct() throws InterruptedException {
        // given
        Coupon coupon = Coupon.create("쿠폰", CouponType.TOTAL, DiscountType.FIXED, 1000, 3, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), 10);
        jpaCouponRepository.save(coupon);
        int threadCount = 13;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        Long couponId = coupon.getId();

        // when
        for (long i = 1; i <= threadCount; i++) {
            executorService.submit(() -> {
                try {
                    couponService.deduct(couponId);
                } catch (Exception e) {
                    // 예외 로깅만 하고 넘어가기
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 작업이 끝날 때까지 대기

        // then
        Coupon findCoupon = jpaCouponRepository.findById(couponId).orElseThrow();
        assertThat(findCoupon.getQuantity()).isEqualTo(0);
    }

}
