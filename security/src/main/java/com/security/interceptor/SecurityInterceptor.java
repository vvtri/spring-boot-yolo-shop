package com.security.interceptor;

import com.security.util.TokenProvider;
import com.yoloshop.annotation.Authenticate;
import com.yoloshop.annotation.Authorize;
import com.yoloshop.common.Constant;
import com.yoloshop.entity.User;
import com.yoloshop.enumeration.ClaimKey;
import com.yoloshop.enumeration.Role;
import com.yoloshop.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;

public class SecurityInterceptor implements HandlerInterceptor {

    private UserRepository userRepository;
    private TokenProvider tokenProvider;

    public SecurityInterceptor(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ((handler instanceof HandlerMethod) == false) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        if (method.isAnnotationPresent(Authorize.class)) {
            Authorize authorizeAnno = method.getDeclaredAnnotation(Authorize.class);
            Role requiredRole = authorizeAnno.role();
            return authorization(request,response,requiredRole);
        }

        if (method.isAnnotationPresent(Authenticate.class)) {
            return authentication(request, response);
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    public boolean authentication(HttpServletRequest request, HttpServletResponse response) {
        String token = resolveToken(request);
        tokenProvider.validateAndParseToken(token);
        return true;
    }

    public boolean authorization(HttpServletRequest request, HttpServletResponse response, Role requiredRole) {

        String token = resolveToken(request);
        Claims claims = tokenProvider.validateAndParseToken(token);
        Role[] roles = claims.get(ClaimKey.USER_ROLES.name(), Role[].class);

        boolean isAuthorized = Arrays.stream(roles).anyMatch(role -> role.equals(requiredRole));
        if(!isAuthorized) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbid");

        return true;
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(Constant.Header.AUTHORIZATION);
        if (!StringUtils.hasText(bearerToken) || bearerToken.startsWith(Constant.Header.TOKEN_TYPE)) {
            return null;
        }
        return bearerToken.substring(Constant.Header.TOKEN_TYPE.length() + 1);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
