package kr.hhplus.be.domain.coupon;

import kr.hhplus.be.domain.user.User;
import kr.hhplus.be.infra.coupon.JpaCouponRepository;
import kr.hhplus.be.infra.user.JpaUserRepository;
import kr.hhplus.be.support.exception.AlreadyIssuedException;
import kr.hhplus.be.support.exception.CouponIssueLimitExceededException;
import kr.hhplus.be.support.exception.CouponIssuePeriodException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create"
})
@Transactional
class CouponServiceIntegrationTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private CouponService couponService;

    @Autowired
    private JpaCouponRepository jpaCouponRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @AfterEach
    void tearDown() {
        redisTemplate.delete(redisTemplate.keys("*"));
    }

    @DisplayName("쿠폰을 저장할 수 있다.")
    @Test
    void saveCoupon() {
        // given
        CouponCommand.Register command = new CouponCommand.Register("5000원 반짝 쿠폰", CouponType.TOTAL, DiscountType.FIXED, 5000, 3, LocalDate.now().minusDays(1), LocalDate.now().plusDays(3), 10);

        // when
        Coupon coupon = couponService.register(command);

        // then
        Coupon findCoupon = jpaCouponRepository.findById(coupon.getId()).orElseThrow();
        assertThat(findCoupon).isNotNull();
    }

    @DisplayName("발급 가능 수량을 초괴한 경우 CouponIssueLimitExceededException이 발생한다.")
    @Test
    void overIssueLimit() {
        // given
        Coupon coupon = Coupon.create("4월 반짝 쿠폰", CouponType.TOTAL, DiscountType.FIXED, 5000, 3, LocalDate.now(), LocalDate.now().plusDays(3), 0);
        jpaCouponRepository.save(coupon);

        // when
        assertThatThrownBy(() -> couponService.issueValidate(coupon.getId()))
                .isInstanceOf(CouponIssueLimitExceededException.class)
                .hasMessage("발급 가능한 수량을 초과하였습니다.");
    }

    @DisplayName("발급 가능 수량을 초괴한 경우 CouponIssueLimitExceededException이 발생한다.")
    @Test
    void notIssuePeriod() {
        // given
        Coupon coupon = Coupon.create("4월 반짝 쿠폰", CouponType.TOTAL, DiscountType.FIXED, 5000, 3
                , LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), 10);
        jpaCouponRepository.save(coupon);

        // when
        assertThatThrownBy(() -> couponService.issueValidate(coupon.getId()))
                .isInstanceOf(CouponIssuePeriodException.class)
                .hasMessage("쿠폰 발급 기간이 아닙니다.");
    }

    @Nested
    class issueCall{
        @DisplayName("쿠폰 발급을 요청하면 redis에 저장된다.")
        @Test
        void issueCallCoupon() {
            // given
            User user = jpaUserRepository.save(User.create("yeop"));
            CouponCommand.IssueCall command = new CouponCommand.IssueCall(user, 1L);
            // when
            couponService.issueCall(command);

            // then
            String key = "coupon:"+ command.couponId() +":issueCall";
            assertThat(redisTemplate.hasKey(key)).isTrue();
            assertThat(redisTemplate.opsForSet().size(key)).isEqualTo(1);

        }

        @DisplayName("이미 발급 요청된 유저가 다시 요청하는 경우 AlreadyIssuedException이 발생한다.")
        @Test
        void callIssue_alreadyIssuedCall() {
            // given
            User user = jpaUserRepository.save(User.create("yeop"));
            Long couponId = 1L;
            CouponCommand.IssueCall command = new CouponCommand.IssueCall(user, couponId);

            final String KEY_PREFIX = "coupon:";
            final String CALL_KEY_SUFFIX = ":issueCall";
            redisTemplate.opsForSet().add(KEY_PREFIX + couponId + CALL_KEY_SUFFIX, "userId:" + user.getId());
            // when
            assertThatThrownBy(() -> couponService.issueCall(command))
                    .isInstanceOf(AlreadyIssuedException.class)
                    .hasMessage("이미 발급 요청한 쿠폰입니다.");
        }
    }

}