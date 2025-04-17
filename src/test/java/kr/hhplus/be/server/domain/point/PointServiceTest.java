package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    @Mock
    private PointRepository pointRepository;

    @InjectMocks
    private PointService pointService;

    @Nested
    class find{
        @DisplayName("userId에 해당하는 유저의 포인트를 조회할 수 있다.")
        @Test
        void success() {
            // given
            Long userId = 1L;
            User user = User.create("yeop");
            when(pointRepository.findByUserId(userId))
                    .thenReturn(Optional.of(Point.create(user, 0)));

            // when
            Point point = pointService.find(userId);

            // then
            assertThat(point.getUserId()).isEqualTo(user.getId());
            assertThat(point.getBalance()).isEqualTo(0);
            verify(pointRepository, times(1)).findByUserId(userId);
        }

        @DisplayName("userId에 해당하는 유저가 없는 경우 IllegalArgumentException이 발생한다.")
        @Test
        void fail() {
            // given
            Long userId = 1L;

            // when // then
            assertThatThrownBy(() -> pointService.find(userId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("등록되지 않은 회원입니다.");

            verify(pointRepository, times(1)).findByUserId(userId);
        }
    }

    @Nested
    class charge {
        @DisplayName("userId에 해당하는 유저의 포인트를 amount만큼 충전합니다.")
        @Test
        void success() {
            // given
            Long userId = 1L;
            User user = User.create("yeop");
            int originalAmount = 10;
            Point point = Point.create(user, originalAmount);
            int chargeAmount = 10;
            when(pointRepository.findByUserId(userId)).thenReturn(Optional.of(point));

            PointCommand.Charge command = new PointCommand.Charge(userId, chargeAmount);
            // when
            Point charge = pointService.charge(command);

            // then
            assertThat(charge.getBalance()).isEqualTo(originalAmount + chargeAmount);
            verify(pointRepository, times(1)).findByUserId(userId);
        }
    }

}