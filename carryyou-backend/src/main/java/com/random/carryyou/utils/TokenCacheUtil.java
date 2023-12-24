package com.random.carryyou.utils;

import java.util.HashMap;
import java.util.Map;

public class TokenCacheUtil {

    // 默认 token 过期时间，以分钟为单位
    private static final int TOKEN_EXPIRE_TIME = 15;

    public static final Map<String, Integer> CACHE_MAP = new HashMap<>();

    public static void cacheToken(String token) {
        CACHE_MAP.put(token, TOKEN_EXPIRE_TIME);
    }

    public static boolean tokenExist(String token) {
        return CACHE_MAP.containsKey(token);
    }

}
