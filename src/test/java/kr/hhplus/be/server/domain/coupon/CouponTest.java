package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.userCoupon.UserCoupon;
import org.junit.jupiter.api.DisplayName;
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
        Coupon coupon = Coupon.create(1L, "4월 반짝 쿠폰", 5000, 3, initialQuantity);

        // then
        assertThat(coupon.getQuantity()).isEqualTo(initialQuantity);
    }

    @DisplayName("쿠폰으로 멤버쿠폰을 발급하면 남은 개수가 1 줄어든다.")
    @Test
    void issueToDeductQuantity() {
        // given
        Coupon coupon = Coupon.create(1L, "4월 반짝 쿠폰", 5000, 3, 50);
        User user = User.create(1L, "yeop");
        int originalQuantity = coupon.getQuantity();

        // when
        coupon.issueTo(user);

        // then
        assertThat(coupon.getQuantity()).isEqualTo(originalQuantity - 1);
    }

    @DisplayName("쿠폰으로 멤버쿠폰을 발급할 때 남은 개수가 0인 경우 IllegalArgumentException이 발생한다.")
    @Test
    void issueLeftQuantityZero() {
        // given
        Coupon coupon = Coupon.create(1L, "4월 반짝 쿠폰", 5000, 3, 0);
        User user = User.create(1L, "yeop");

        // when
        assertThatThrownBy(() -> coupon.issueTo(user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("발급 가능한 수량을 초과하였습니다.");

        // then
    }

    @DisplayName("쿠폰으로 멤버쿠폰을 발급할 때 멤버쿠폰의 유효기간은 현재 날짜에서 expirationMonth개월 만큼 더한 날까지 사용할 수 있다.")
    @Test
    void issueExpirationByExpirationMonth() {
        // given
        Coupon coupon = Coupon.create(1L, "4월 반짝 쿠폰", 5000, 3, 10);
        User user = User.create(1L, "yeop");
        LocalDate now = LocalDate.now();

        // when
        UserCoupon userCoupon = coupon.issueTo(user);

        // then
        assertThat(userCoupon.getExpiredAt().isEqual(now.plusMonths(coupon.getExpirationMonth()))).isTrue();
    }


}