package com.example.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 配置类，用于创建和配置队列
 */
@Configuration
public class RabbitConfiguration {

    /**
     * 创建一个名为 email 的持久化队列
     *
     * @return 队列实例
     */
    @Bean("emailQueue")
    public Queue emailQueue() {
        // 使用 QueueBuilder 创建一个名为 email 的持久化队列
        return QueueBuilder.durable("email").build();
    }
}
