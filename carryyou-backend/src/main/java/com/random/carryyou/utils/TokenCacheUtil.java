package com.random.carryyou.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class TokenCacheUtil {

    @Value("${token.expire.after.minutes}")
    private int tokenExpireAfterMinutes;

    @PostConstruct
    public void init() {
        log.info("设置 token 默认有效时长: {} 分钟", tokenExpireAfterMinutes);
        TokenCacheUtil.TOKEN_EXPIRE_TIME = this.tokenExpireAfterMinutes;
    }

    // 默认 token 过期时间，以分钟为单位
    private static int TOKEN_EXPIRE_TIME;

    public static final Map<String, Integer> CACHE_MAP = new HashMap<>();

    public static void cacheToken(String token) {
        CACHE_MAP.put(token, TOKEN_EXPIRE_TIME);
    }

    public static boolean tokenExist(String token) {
        return CACHE_MAP.containsKey(token);
    }

}
