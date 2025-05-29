package kr.hhplus.be.domain.userCoupon;

import kr.hhplus.be.support.exception.AlreadyUsedException;
import kr.hhplus.be.support.exception.ExpiredException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserCouponTest {

    @DisplayName("유효기간이 만료되지 않은 경우 ixExpration()은 false이다.")
    @Test
    void isNotExpiration() {
        // given
        LocalDate fixedDate = LocalDate.now();
        UserCoupon userCoupon = UserCoupon.builder().userId(1L)
                .expiredAt(fixedDate).build();
        // when // then
        assertThat(userCoupon.isExpiration()).isFalse();
    }

    @DisplayName("유효기간이 만료된 경우 isExpration()은 true이다.")
    @Test
    void isExpiration() {
        // given
        LocalDate fixedDate = LocalDate.now().minusDays(1);
        UserCoupon userCoupon = UserCoupon.builder().userId(1L)
                                    .expiredAt(fixedDate).build();
        // when // then
        assertThat(userCoupon.isExpiration()).isTrue();
    }

    @Nested
    class validate {

        @DisplayName("정상적인 쿠폰의 경우 exception이 발생하지 않는다.")
        @Test
        void success() {
            // given
            Long userId = 1L;
            UserCoupon userCoupon = UserCoupon.builder()
                    .userId(1L)
                    .couponId(1L)
                    .name("깜짝 쿠폰")
                    .discountAmount(5000)
                    .expiredAt(LocalDate.now().plusDays(30))
                    .build();

            // when
            userCoupon.validate(userId);
        }

        @DisplayName("넘겨받은 userId와 userCoupon의 userId가 동일하지 않은 경우 IllegalArgumentException이 발생한다.")
        @Test
        void userIdValidate() {
            // given
            Long differentUserId = 2L;
            UserCoupon userCoupon = UserCoupon.builder()
                    .userId(1L)
                    .couponId(1L)
                    .name("깜짝 쿠폰")
                    .discountAmount(5000)
                    .expiredAt(LocalDate.now().plusDays(30))
                    .build();

            // when // then
            assertThatThrownBy(() -> userCoupon.validate(differentUserId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("보유 중인 쿠폰이 아닙니다.");
        }

        @DisplayName("유효기간이 만료된 경우 ExpiredException 발생한다.")
        @Test
        void expiredAtValidate() {
            // given
            Long userId = 1L;
            UserCoupon userCoupon = UserCoupon.builder()
                    .userId(userId)
                    .couponId(1L)
                    .name("깜짝 쿠폰")
                    .discountAmount(5000)
                    .expiredAt(LocalDate.now().minusDays(1))
                    .build();

            // when // then
            assertThatThrownBy(() -> userCoupon.validate(userId))
                    .isInstanceOf(ExpiredException.class)
                    .hasMessage("유효기간이 만료된 쿠폰입니다.");
        }

        @DisplayName("사용된 쿠폰인 경우 AlreadyUsedException이 발생한다.")
        @Test
        void alreadyUsedException() {
            // given
            Long userId = 1L;
            UserCoupon userCoupon = UserCoupon.builder()
                    .userId(userId)
                    .couponId(1L)
                    .name("깜짝 쿠폰")
                    .discountAmount(5000)
                    .expiredAt(LocalDate.now().plusDays(1))
                    .usedAt(LocalDateTime.now().minusDays(1))
                    .build();

            // when // then
            assertThatThrownBy(() -> userCoupon.validate(userId))
                    .isInstanceOf(AlreadyUsedException.class)
                    .hasMessage("이미 사용된 쿠폰입니다.");
        }
    }

    @Nested
    class use {


        @DisplayName("정상적인 쿠폰과 유저인 경우 사용처리가 가능하다.")
        @Test
        void success() {
            // given
            Long userId = 1L;
            Long orderId = 1L;
            UserCoupon userCoupon = UserCoupon.builder()
                    .userId(userId)
                    .couponId(1L)
                    .name("깜짝 쿠폰")
                    .discountAmount(5000)
                    .expiredAt(LocalDate.now().plusMonths(1))
                    .build();

            // when
            userCoupon.use(userId);

            // then
            assertThat(userCoupon.getUsedAt()).isNotNull();
        }

        @DisplayName("쿠폰을 사용할 때 userId가 다른 경우 IllegalArgumentException이 발생한다.")
        @Test
        void failByNotEqualsUserId() {
            // given
            Long userId = 1L;
            Long orderId = 1L;
            UserCoupon userCoupon = UserCoupon.builder()
                    .userId(userId)
                    .couponId(1L)
                    .name("깜짝 쿠폰")
                    .discountAmount(5000)
                    .expiredAt(LocalDate.now().plusMonths(1))
                    .build();

            Long differentUserId = 2L;

            // when // then
            assertThatThrownBy(() -> userCoupon.use(differentUserId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("보유 중인 쿠폰이 아닙니다.");;
        }

        @DisplayName("쿠폰을 사용할 때 유효기간이 지난 경우 ExpiredException 발생한다.")
        @Test
        void failExpired() {
            // given
            Long userId = 1L;
            Long orderId = 1L;
            UserCoupon userCoupon = UserCoupon.builder()
                    .userId(userId)
                    .couponId(1L)
                    .name("깜짝 쿠폰")
                    .discountAmount(5000)
                    .expiredAt(LocalDate.now().minusDays(1))
                    .build();

            // when // then
            assertThatThrownBy(() -> userCoupon.use(userId))
                    .isInstanceOf(ExpiredException.class)
                    .hasMessage("유효기간이 만료된 쿠폰입니다.");
        }

        @DisplayName("사용된 쿠폰인 경우 AlreadyUsedException이 발생한다.")
        @Test
        void alreadyUsedException() {
            // given
            Long userId = 1L;
            UserCoupon userCoupon = UserCoupon.builder()
                    .userId(userId)
                    .couponId(1L)
                    .name("깜짝 쿠폰")
                    .discountAmount(5000)
                    .expiredAt(LocalDate.now().plusDays(1))
                    .usedAt(LocalDateTime.now().minusDays(1))
                    .build();

            // when // then
            assertThatThrownBy(() -> userCoupon.validate(userId))
                    .isInstanceOf(AlreadyUsedException.class)
                    .hasMessage("이미 사용된 쿠폰입니다.");
        }
    }

}