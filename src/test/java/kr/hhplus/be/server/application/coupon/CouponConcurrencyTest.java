package kr.hhplus.be.server.application.coupon;


import kr.hhplus.be.domain.coupon.Coupon;
import kr.hhplus.be.domain.coupon.CouponType;
import kr.hhplus.be.domain.coupon.DiscountType;
import kr.hhplus.be.domain.user.User;
import kr.hhplus.be.domain.userCoupon.UserCouponCommand;
import kr.hhplus.be.domain.userCoupon.UserCouponService;
import kr.hhplus.be.infra.coupon.JpaCouponRepository;
import kr.hhplus.be.infra.user.JpaUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.time.LocalDate;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CouponConcurrencyTest {

    @Autowired
    private UserCouponService userCouponService;

    @Autowired
    private JpaCouponRepository jpaCouponRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    void setUp() {
        redisTemplate.delete(redisTemplate.keys("*"));
    }

    @AfterEach
    void tearDown() {
        redisTemplate.delete(redisTemplate.keys("*"));
    }

    @DisplayName("동일한 쿠폰에 대한 발급을 요청한 경우 발급된 개수만큼 callIssue 캐시에 쌓이게 된다.")
    @Test
    void callIssueByCoupon() throws InterruptedException {
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
                    UserCouponCommand.CallIssue command = new UserCouponCommand.CallIssue(user, couponId);
                    // when
                    userCouponService.callIssueUserCoupon(command);
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

        String key = "coupon:"+ coupon.getId() +":callIssue";
        assertThat(redisTemplate.hasKey(key)).isTrue();
        Set<ZSetOperations.TypedTuple<Object>> typedTuples = redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, -1);
        assertThat(typedTuples).hasSize(threadCount);
    }

    @DisplayName("동일한 유저가 동일한 쿠폰에 대한 발급 요청을 여러번 하는 경우 callIssued 캐시에는 1개의 요청만 등록된다.")
    @Test
    void issueByCoupon() throws InterruptedException {
        // given
        Coupon coupon = Coupon.create("쿠폰", CouponType.TOTAL, DiscountType.FIXED, 1000, 3, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), 10);
        jpaCouponRepository.save(coupon);

        int threadCount = 13;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successCnt = new AtomicInteger();

        User user = User.create("user");
        jpaUserRepository.save(user);

        Long couponId = coupon.getId();

        // when
        for (long i = 1; i <= threadCount; i++) {
            executorService.submit(() -> {
                try {
                    UserCouponCommand.CallIssue command = new UserCouponCommand.CallIssue(user, couponId);
                    // when
                    userCouponService.callIssueUserCoupon(command);
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

        String key = "coupon:"+ coupon.getId() +":callIssue";
        assertThat(redisTemplate.hasKey(key)).isTrue();
        Set<ZSetOperations.TypedTuple<Object>> typedTuples = redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, -1);
        assertThat(typedTuples).hasSize(1);
    }
}