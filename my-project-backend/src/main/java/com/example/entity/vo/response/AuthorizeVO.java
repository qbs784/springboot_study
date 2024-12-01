package com.example.entity.vo.response;

import lombok.Data;

import java.util.Date;

/**
 * 表示授权信息的类，包括用户名、角色、令牌和过期时间
 */
@Data
public class AuthorizeVO {
    /**
     * 用户名
     */
    String username;
    /**
     * 角色
     */
    String role;
    /**
     * 令牌
     */
    String token;
    /**
     * 过期时间
     */
    Date expire;

}
