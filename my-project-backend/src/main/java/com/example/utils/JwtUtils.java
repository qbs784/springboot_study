package com.example.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtils {

    @Resource
    StringRedisTemplate template;

    @Value("${spring.security.jwt.key}")
    String key;

    @Value("${spring.security.jwt.expire}")
    int expire;

    /**
     * 使 JWT 失效
     *
     * @param headerToken 包含 JWT 的 HTTP 头部字符串
     * @return 如果 JWT 成功失效，返回 true；否则返回 false
     */
    public boolean invalidateJwt(String headerToken) {
        String token = this.convertToken(headerToken);
        if (token == null) return false;
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier verifier = JWT.require(algorithm).build();
        try {
            DecodedJWT jwt = verifier.verify(token);
            String id = jwt.getId();
            return deleteToken(id, jwt.getExpiresAt());
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    /**
     * 删除 Redis 中的 JWT
     *
     * @param uuid JWT 的唯一标识符
     * @param time JWT 的过期时间
     * @return 如果 JWT 成功删除，返回 true；否则返回 false
     */
    private boolean deleteToken(String uuid, Date time) {
        if (this.isInvalidToken(uuid)) return false;
        Date now = new Date();
        long expire = Math.max(time.getTime() - now.getTime(), 0);
        template.opsForValue().set(Const.JWT_BLACKLIST + uuid, "", expire, TimeUnit.MICROSECONDS);
        return true;
    }

    /**
     * 检查 JWT 是否已经失效
     *
     * @param uuid JWT 的唯一标识符
     * @return 如果 JWT 已经失效，返回 true；否则返回 false
     */
    private boolean isInvalidToken(String uuid) {
        return Boolean.TRUE.equals(template.hasKey(Const.JWT_BLACKLIST + uuid));
    }

    /**
     * 解码 JWT
     *
     * @param headerToken 包含 JWT 的 HTTP 头部字符串
     * @return 解码后的 JWT，如果 JWT 无效或过期，返回 null
     */
    public DecodedJWT decode(String headerToken) {
        String token = this.convertToken(headerToken);
        if (token == null) return null;
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier verifier = JWT.require(algorithm).build();
        try {
            DecodedJWT jwt = verifier.verify(token);
            if (this.isInvalidToken(jwt.getId())) return null;
            Date expiresAt = jwt.getExpiresAt();
            return new Date().after(expiresAt) ? null : jwt;
        } catch (JWTVerificationException e) {
            return null;
        }

    }

    /**
     * 生成 JWT
     *
     * @param userDetails 用户详细信息
     * @param id          用户 ID
     * @param username    用户名
     * @return 生成的 JWT
     */
    public String generateJWT(UserDetails userDetails, int id, String username) {
        Algorithm algorithm = Algorithm.HMAC256(key);
        Date expire = this.expireTime();
        return JWT.create()
                .withJWTId(UUID.randomUUID().toString())
                .withClaim("id", id)
                .withClaim("name", username)
                .withClaim("authorities", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .withExpiresAt(expire)
                .withIssuedAt(new Date())
                .sign(algorithm);
    }

    /**
     * 计算 JWT 的过期时间
     *
     * @return JWT 的过期时间
     */
    public Date expireTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, expire * 24);
        return calendar.getTime();
    }

    /**
     * 从 HTTP 头部字符串中提取 JWT
     *
     * @param token 包含 JWT 的 HTTP 头部字符串
     * @return 提取出的 JWT，如果格式不正确，返回 null
     */
    private String convertToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return null;
        }
        return token.substring(7);
    }

    /**
     * 将解码后的 JWT 转换为 UserDetails 对象
     *
     * @param decodedJWT 解码后的 JWT
     * @return UserDetails 对象
     */
    public UserDetails toUser(DecodedJWT decodedJWT) {
        Map<String, Claim> claims = decodedJWT.getClaims();
        return User
                .withUsername(claims.get("name").asString())
                .password("******")
                .authorities(claims.get("authorities").asArray(String.class))
                .build();
    }

    /**
     * 从解码后的 JWT 中提取用户 ID
     *
     * @param decodedJWT 解码后的 JWT
     * @return 用户 ID
     */
    public Integer toId(DecodedJWT decodedJWT) {
        Map<String, Claim> claims = decodedJWT.getClaims();
        return claims.get("id").asInt();
    }
}
