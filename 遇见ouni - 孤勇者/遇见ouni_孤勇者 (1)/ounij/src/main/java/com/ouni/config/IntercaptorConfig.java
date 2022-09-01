package com.ouni.config;

import com.ouni.interceptor.JWTInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description: JWT拦截器
 */
@Configuration
public class IntercaptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JWTInterceptor())
                //拦截的路径
//                .addPathPatterns("/**")
                //排除接口
//                .excludePathPatterns("/user/login","/user/jscode2session");
                .excludePathPatterns("/**");
    }
}
