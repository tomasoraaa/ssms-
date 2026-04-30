package com.lgs.config;

import com.lgs.interceptor.PasswordCheckInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置类
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private PasswordCheckInterceptor passwordCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册密码变更检测拦截器
        registry.addInterceptor(passwordCheckInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/register", "/", "/files/**");
    }
}
