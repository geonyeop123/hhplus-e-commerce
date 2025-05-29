package kr.hhplus.be.server.support.resolver;

import kr.hhplus.be.domain.user.User;
import kr.hhplus.be.server.interfaces.common.CurrentUser;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class CurrentUserResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter p) {
        return p.hasParameterAnnotation(CurrentUser.class)
                && p.getParameterType().equals(User.class);
    }
    @Override
    public Object resolveArgument(MethodParameter p, ModelAndViewContainer mav,
                                  NativeWebRequest webRequest, WebDataBinderFactory f) {
        return webRequest.getAttribute("currentUser", RequestAttributes.SCOPE_REQUEST);
    }
}
