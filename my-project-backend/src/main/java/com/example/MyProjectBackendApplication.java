package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 这是一个 Spring Boot 应用程序的入口类。
 * 它使用 @SpringBootApplication 注解来自动配置 Spring 应用上下文。
 */
@SpringBootApplication
public class MyProjectBackendApplication {

    /**
     * 应用程序的主方法，用于启动 Spring Boot 应用程序。
     *
     * @param args 命令行参数。
     */
    public static void main(String[] args) {
        // 运行 Spring Boot 应用程序，传入当前类和命令行参数
        SpringApplication.run(MyProjectBackendApplication.class, args);
    }

}
