package kr.hhplus.be.domain.point;

import kr.hhplus.be.domain.user.User;
import kr.hhplus.be.support.exception.InsufficientBalanceException;
import kr.hhplus.be.support.exception.MaximumBalanceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PointTest {

    @Nested
    class charge{

        @DisplayName("포인트를 충전하면 기존 포인트와 충전할 포인트를 합산한 포인트를 보유한다.")
        @Test
        void success() {
            // given
            int amount = 1;
            int originalAmount = 10;
            Point point = createPoint(originalAmount);

            // when
            point.charge(amount);

            // then
            assertThat(point.getBalance()).isEqualTo(amount + originalAmount);
        }

        @DisplayName("포인트 충전을 1원보다 작게 충전하는 경우 IllegalArgumentException가 발생한다.")
        @Test
        void fail() {
            // given
            int amount = 0;
            Point point = createPoint(amount);

            // when // then
            assertThatThrownBy(() -> point.charge(amount))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("포인트는 1포인트 이상부터 충전이 가능합니다.");
        }

        @DisplayName("포인트 충전 이후 잔고가 10,000,000원 이상인 경우 IllegalArgumentException이 발생한다.")
        @Test
        void fail2() {
            // given
            int amount = 10_000_001;
            Point point = createPoint(0);

            // when // then
            assertThatThrownBy(() -> point.charge(amount))
                    .isInstanceOf(MaximumBalanceException.class)
                    .hasMessage("충전 이후 포인트는 10,000,000포인트를 넘을 수 없습니다.");
        }
    }

    @Nested
    class use{
        @DisplayName("포인트를 사용하면, 기존 포인트에서 사용할 포인트를 차감한 포인트를 보유한다.")
        @Test
        void success() {
            // given
            int amount = 10;
            int originalAmount = 15;
            Point point = createPoint(originalAmount);

            // when
            point.use(amount);

            // then
            assertThat(point.getBalance()).isEqualTo(originalAmount - amount);
        }

        @DisplayName("0포인트 이하를 사용할 경우 IllegalArgumentException이 발생한다.")
        @Test
        void fail() {
            // given
            int amount = 0;
            int originalAmount = 10;
            Point point = createPoint(originalAmount);

            // when // then
            assertThatThrownBy(() -> point.use(amount))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("포인트는 1포인트 이상부터 사용 가능합니다.");
        }

        @DisplayName("사용할 포인트보다 잔고가 부족한 경우 IllegalArgumentException이 발생한다.")
        @Test
        void fail2() {
            // given
            int amount = 11;
            int originalAmount = 10;
            Point point = createPoint(originalAmount);

            // when // then
            assertThatThrownBy(() -> point.use(amount))
                    .isInstanceOf(InsufficientBalanceException.class)
                    .hasMessage("보유 포인트가 부족합니다.");
        }
    }

    private Point createPoint(int balance){
        return Point.create(User.create("yeop"), balance);
    }
}