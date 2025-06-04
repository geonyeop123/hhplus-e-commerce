package kr.hhplus.be.server.support.config;

import kr.hhplus.be.server.support.interceptor.UserAuthenticationInterceptor;
import kr.hhplus.be.server.support.resolver.CurrentUserResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final UserAuthenticationInterceptor userInterceptor;

    @Autowired
    public WebMvcConfig(UserAuthenticationInterceptor userInterceptor) {
        this.userInterceptor = userInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/api/**/coupons/**")
                .addPathPatterns("/api/**/points/**")
                .addPathPatterns("/api/**/orders/**");

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CurrentUserResolver());
    }
}
