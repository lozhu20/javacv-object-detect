package com.random.carryyou.config;

import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Springboot3 引入 PageHelper 无效，新增该配置，
 * 切换 springboot2 后提示检测到多个 PageHelper 配置，需取消
 */
@Configuration
public class PageHelperConfig {

//    @Bean
//    public Interceptor[] plugins() {
//        return new Interceptor[]{new PageInterceptor()};
//    }

}
