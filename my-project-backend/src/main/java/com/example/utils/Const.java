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

    //请求自定义属性
    public final static String ATTR_USER_ID = "id";

    public final static String MQ_MAIL = "mail";

    //用户角色
    public final static String ROLE_DEFAULT = "user";
    public final static String ROLE_ADMIN = "admin";
    public static final String FORUM_IMAGE_COUNTER = "forum:image:";
}
