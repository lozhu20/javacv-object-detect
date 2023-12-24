package com.random.carryyou.job;

import com.random.carryyou.utils.TokenCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class ClearExpiredTokenJob {

    // 每分钟运行一次
    @Scheduled(cron = "0 * * * * ?")
    public void clearExpiredToken() {
        Map<String, Integer> cacheMap = TokenCacheUtil.CACHE_MAP;
        cacheMap.entrySet().forEach(e -> e.setValue(e.getValue() - 1));
        cacheMap.entrySet().removeIf(cache -> cache.getValue() <= 0);
        log.info("token 整理结束，现存有效 token {} 个", TokenCacheUtil.CACHE_MAP.size());
    }
}
