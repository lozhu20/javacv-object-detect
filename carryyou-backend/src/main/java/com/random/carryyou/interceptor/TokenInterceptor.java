package com.random.carryyou.interceptor;

import com.random.carryyou.utils.TokenCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    private final List<String> WHITE_URI_LIST = Arrays.asList("login", "register", "download");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IllegalAccessException {

        String requestURI = request.getRequestURI();
        // 如果是注册/登陆请求，则放行
        for (String uri : WHITE_URI_LIST) {
            if (requestURI.contains(uri)) {
                return true;
            }
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
