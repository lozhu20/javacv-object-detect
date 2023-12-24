package com.random.carryyou.interceptor;

import com.random.carryyou.utils.TokenCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IllegalAccessException {

        String requestURI = request.getRequestURI();
        // 如果是注册/登陆请求，则放行
        if (requestURI.contains("login") || requestURI.contains("register")) {
            return true;
        }
        String token = request.getHeader("token");
        if (TokenCacheUtil.CACHE_MAP.containsKey(token)) {
            return true;
        }
        throw new IllegalAccessException("登陆信息已超期，请重新登陆");
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }
}
