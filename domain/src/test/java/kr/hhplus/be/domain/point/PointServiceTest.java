package kr.hhplus.be.domain.point;

import kr.hhplus.be.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    @Mock
    private PointRepository pointRepository;

    @InjectMocks
    private PointService pointService;

    @Nested
    class find{
        @DisplayName("유저에 해당하는 포인트를 조회할 수 있다.")
        @Test
        void success() {
            // given
            User user = User.create("yeop");
            Long userId = user.getId();

            when(pointRepository.findByUserId(userId))
                    .thenReturn(Optional.of(Point.create(user, 0)));

            // when
            Point point = pointService.find(user);

            // then
            assertThat(point.getBalance()).isEqualTo(0);
            verify(pointRepository, times(1)).findByUserId(userId);
        }
    }

    @Nested
    class charge {
        @DisplayName("유저의 포인트를 amount만큼 충전합니다.")
        @Test
        void success() {
            // given
            User user = User.create("yeop");
            Long userId = user.getId();

            int originalAmount = 10;
            Point point = Point.create(user, originalAmount);
            int chargeAmount = 10;
            when(pointRepository.findByUserIdForUpdate(userId)).thenReturn(Optional.of(point));

            PointCommand.Charge command = new PointCommand.Charge(user, chargeAmount);
            // when
            Point charge = pointService.charge(command);

            // then
            assertThat(charge.getBalance()).isEqualTo(originalAmount + chargeAmount);
            verify(pointRepository, times(1)).findByUserIdForUpdate(userId);
        }
    }

}