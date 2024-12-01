package com.example.utils;

public class Const {
    /**
     * JWT 黑名单的 Redis 键前缀
     */
    public static final String JWT_BLACKLIST = "jwt:blacklist:";

    /**
     * 验证电子邮件限制的 Redis 键前缀
     */
    public static final String VERIFY_EMAIL_LIMIT = "verify:email:limit:";

    /**
     * 验证电子邮件数据的 Redis 键前缀
     */
    public static final String VERIFY_EMAIL_DATA = "verify:email:data:";

    /**
     * 订单的 CORS 优先级
     */
    public static final int ORDER_CORS = -102;

    /**
     * 订单的限制优先级
     */
    public static final int ORDER_LIMIT = -101;

    /**
     * 流量限制计数器的 Redis 键前缀
     */
    public final static String FLOW_LIMIT_COUNTER = "flow:counter:";

    /**
     * 流量限制阻塞的 Redis 键前缀
     */
    public final static String FLOW_LIMIT_BLOCK = "flow:block:";
}
