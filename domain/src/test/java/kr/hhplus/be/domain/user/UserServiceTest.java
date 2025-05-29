package kr.hhplus.be.domain.user;

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
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Nested
    class find{
        @DisplayName("올바른 userId로 User를 조회할 수 있다.")
        @Test
        void success() {
            // given
            Long userId = 1L;
            User user = User.create("yeop");
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            // when
            User findUser = userService.findById(userId);

            // then
            assertThat(findUser).isEqualTo(user);
            verify(userRepository, times(1)).findById(userId);
        }

        @DisplayName("userId에 해당하는 회원이 없는 경우 IllegalArgumentException이 발생한다.")
        @Test
        void fail() {
            // given
            Long userId = 1L;

            // when // then
            assertThatThrownBy(() -> userService.findById(userId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("등록되지 않은 회원입니다.");
        }
    }



}