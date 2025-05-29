package kr.hhplus.be.domain.coupon;

import kr.hhplus.be.infra.coupon.JpaCouponRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create"
})
class CouponServiceIntegrationTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private CouponService couponService;

    @Autowired
    private JpaCouponRepository jpaCouponRepository;

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

    @DisplayName("유효성 검사에 실패한 경우 coupon의 상태가 finish로 변경된다.")
    @Test
    void issueValidate() {
        // given
        Coupon coupon = Coupon.create("4월 반짝 쿠폰", CouponType.TOTAL, DiscountType.FIXED, 5000, 3, LocalDate.now(), LocalDate.now().plusDays(3), 0);
        jpaCouponRepository.save(coupon);

        // when
        couponService.issueValidate(coupon.getId());

        // then
        Coupon findCoupon = jpaCouponRepository.findById(coupon.getId()).orElseThrow();
        assertThat(findCoupon.getIssueStatus()).isEqualTo(IssueStatus.FINISH);
    }

}