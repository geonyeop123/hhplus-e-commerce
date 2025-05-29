package kr.hhplus.be.domain.coupon;

import kr.hhplus.be.domain.user.User;
import kr.hhplus.be.domain.userCoupon.UserCoupon;
import kr.hhplus.be.support.exception.CouponIssueLimitExceededException;
import kr.hhplus.be.support.exception.CouponIssuePeriodException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CouponTest {

    @DisplayName("쿠폰을 생성하면 최초 개수가 현재 개수가 된다.")
    @Test
    void create() {
        // given
        int initialQuantity = 50;

        // when
        Coupon coupon = Coupon.create("4월 반짝 쿠폰", CouponType.TOTAL, DiscountType.FIXED, 5000, 3, LocalDate.now(), LocalDate.now().plusDays(3), initialQuantity);

        // then
        assertThat(coupon.getQuantity()).isEqualTo(initialQuantity);
    }


    @Nested
    class issueValidation{
        @DisplayName("쿠폰의 발급 가능 개수가 0인 경우 유효성 검사에 실패하며 false를 반환한다.")
        @Test
        void issueLeftQuantityZero() {
            // given
            Coupon coupon = Coupon.create("4월 반짝 쿠폰", CouponType.TOTAL, DiscountType.FIXED, 5000, 3,
                    LocalDate.now(), LocalDate.now().plusDays(3), 0);
            LocalDate now = LocalDate.now();

            // when // then
            assertThatThrownBy(() -> coupon.isValid(now)).isInstanceOf(CouponIssueLimitExceededException.class)
                    .hasMessage("발급 가능한 수량을 초과하였습니다.");

        }

        @DisplayName("현재 날짜가 쿠폰의 발급 기간이 아닌 경우 유효성 검사에 실패하며 false를 반환한다.")
        @Test
        void notIssuePeriod() {
            // given
            Coupon coupon = Coupon.create("4월 반짝 쿠폰", CouponType.TOTAL, DiscountType.FIXED, 5000, 3
                    , LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), 10);
            LocalDate now = LocalDate.now();

            // when // then
            assertThatThrownBy(() -> coupon.isValid(now)).isInstanceOf(CouponIssuePeriodException.class)
                    .hasMessage("쿠폰 발급 기간이 아닙니다.");

        }
    }

    @Nested
    class issueTo {
        @DisplayName("쿠폰으로 멤버쿠폰을 발급할 수 있다.")
        @Test
        void issueUserCoupon() {
            // given
            Coupon coupon = Coupon.create("4월 반짝 쿠폰", CouponType.TOTAL, DiscountType.FIXED, 5000, 3, LocalDate.now(), LocalDate.now().plusDays(3), 50);
            User user = User.create("yeop");
            int originalQuantity = coupon.getQuantity();
            // when
            UserCoupon userCoupon = coupon.issueTo(user);

            // then
            assertThat(userCoupon).isNotNull();
        }

        @DisplayName("쿠폰으로 멤버쿠폰을 발급할 때 멤버쿠폰의 유효기간은 현재 날짜에서 expirationMonth개월 만큼 더한 날까지 사용할 수 있다.")
        @Test
        void issueExpirationByExpirationMonth() {
            // given
            Coupon coupon = Coupon.create("4월 반짝 쿠폰", CouponType.TOTAL, DiscountType.FIXED, 5000, 3, LocalDate.now(), LocalDate.now().plusDays(3), 10);
            User user = User.create("yeop");
            LocalDate now = LocalDate.now();

            // when
            UserCoupon userCoupon = coupon.issueTo(user);

            // then
            assertThat(userCoupon.getExpiredAt().isEqual(now.plusMonths(coupon.getExpirationMonth()))).isTrue();
        }
    }

    @Nested
    class deduct{
        @DisplayName("현재 남은 수량이 0 이하인 경우 개수 차감에 실패한다.")
        @Test
        void issueLeftQuantityZero() {
            // given
            Coupon coupon = Coupon.create("4월 반짝 쿠폰", CouponType.TOTAL, DiscountType.FIXED, 5000, 3
                    , LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), 0);

            // when // then
            assertThatThrownBy(coupon::deductQuantity)
                    .isInstanceOf(CouponIssueLimitExceededException.class)
                    .hasMessage("발급 가능한 수량을 초과하였습니다.");
        }

        @DisplayName("쿠폰의 개수를 차감 시도하면 보유 개수에서 1개 차감된다.")
        @Test
        void test() {
            // given
            Coupon coupon = Coupon.create("4월 반짝 쿠폰", CouponType.TOTAL, DiscountType.FIXED, 5000, 3
                    , LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), 5);

            // when
            coupon.deductQuantity();

            // then
            assertThat(coupon.getQuantity()).isEqualTo(4);
        }
    }

}