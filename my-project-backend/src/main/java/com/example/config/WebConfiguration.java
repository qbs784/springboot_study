package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Spring 配置类，用于配置 Web 相关的组件，如密码编码器。
 */
@Configuration
public class WebConfiguration {

    /**
     * 创建并配置一个 BCryptPasswordEncoder Bean，用于密码的加密处理。
     *
     * @return 一个新的 BCryptPasswordEncoder 实例。
     */
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
