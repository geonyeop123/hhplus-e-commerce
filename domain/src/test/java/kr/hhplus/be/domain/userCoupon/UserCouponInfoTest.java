package kr.hhplus.be.domain.userCoupon;

import kr.hhplus.be.domain.coupon.CouponType;
import kr.hhplus.be.domain.coupon.DiscountType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class UserCouponInfoTest {

    @DisplayName("")
    @Test
    void create() {
        // given
        UserCoupon userCoupon = UserCoupon.builder().userId(1L).couponId(1L)
                .name("깜짝 쿠폰").discountType(DiscountType.FIXED).type(CouponType.TOTAL)
                .expiredAt(LocalDate.now()).discountAmount(5000).build();

        // when
        UserCouponInfo info = UserCouponInfo.from(userCoupon);

        // then
        assertThat(info.id()).isEqualTo(userCoupon.getId());
        assertThat(info.name()).isEqualTo(userCoupon.getName());
        assertThat(info.type()).isEqualTo(userCoupon.getType());
        assertThat(info.discountType()).isEqualTo(userCoupon.getDiscountType());
        assertThat(info.discountAmount()).isEqualTo(userCoupon.getDiscountAmount());
        assertThat(info.expiredAt()).isEqualTo(userCoupon.getExpiredAt());
    }

    @DisplayName("")
    @Test
    void test() {
        // given // when
        UserCouponInfo info = UserCouponInfo.empty();

        // then
        assertThat(info.discountAmount()).isEqualTo(0);
    }

}