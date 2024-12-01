package com.example.utils;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 流量控制工具类
 */
@Component
public class FlowUtils {

    @Resource
    StringRedisTemplate template;

    /**
     * 检查指定的 key 是否存在于 Redis 中，如果不存在则将其设置为指定的 blockTime 秒
     *
     * @param key       要检查的 key
     * @param blockTime 阻塞时间，单位为秒
     * @return 如果 key 不存在则返回 true，否则返回 false
     */
    public boolean limitOnceCheck(String key, int blockTime) {
        // 如果 key 存在，则返回 false
        if (Boolean.TRUE.equals(template.hasKey(key))) {
            return false;
        } else {
            // 如果 key 不存在，则将其设置为指定的 blockTime 秒，并返回 true
            template.opsForValue().set(key, "", blockTime, TimeUnit.SECONDS);
            return true;
        }

    }
}
