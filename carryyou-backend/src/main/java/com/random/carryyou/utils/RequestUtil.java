package com.random.carryyou.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
public class RequestUtil {

    public static String getRequestHeader(String headerName) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();
        return request.getHeader(headerName);
    }

    public static String getCurrentUserId() {
        String userId = getRequestHeader("userId");
        log.info("获取到 userId: {}", userId);
        return userId;
    }

    public static List<String> getCurrentRoleList() {
        String roleStr = getRequestHeader("role");
        if (StringUtils.isBlank(roleStr)) {
            return Collections.emptyList();
        }
        return Arrays.asList(roleStr.split(";"));
    }

    public static String getToken() {
        return getRequestHeader("token");
    }

    public static HttpServletResponse getResponse() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        return requestAttributes.getResponse();
    }
}
