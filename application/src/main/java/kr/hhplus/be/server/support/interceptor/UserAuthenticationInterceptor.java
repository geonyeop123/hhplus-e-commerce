package kr.hhplus.be.server.support.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.hhplus.be.domain.user.User;
import kr.hhplus.be.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserAuthenticationInterceptor implements HandlerInterceptor {

    private final UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        // 예: userId를 헤더에서 꺼내서 인증
        String userIdHeader = request.getHeader("X-USER-ID");

        if (userIdHeader == null) {
            throw new IllegalArgumentException("헤더가 누락되었습니다.");
        }

        Long userId;

        try {
            userId = Long.valueOf(userIdHeader);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("유효한 userId가 아닙니다.");
        }

        User user = userService.findById(userId);

        request.setAttribute("currentUser", user);
        return true;  // 다음으로 진행
    }
}
