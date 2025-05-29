package kr.hhplus.be.server.support.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggingFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        long start = System.currentTimeMillis();

        // 요청 정보 로깅
        log.info("[REQUEST] {} {} from {}",
                request.getMethod(),
                request.getRequestURI() +
                        (request.getQueryString() != null ? "?" + request.getQueryString() : ""),
                request.getRemoteAddr());

        // 다음 필터/서블릿 실행
        filterChain.doFilter(request, response);

        // 응답 완료 후 처리 시간 로깅
        long duration = System.currentTimeMillis() - start;
        log.info("[RESPONSE] {} {} -> {} ({}ms)",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                duration);
    }
}
