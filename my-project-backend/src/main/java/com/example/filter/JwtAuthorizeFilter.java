package com.example.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthorizeFilter 是一个自定义的过滤器，用于在每次请求时验证 JWT 并设置认证信息。
 */
@Component
public class JwtAuthorizeFilter extends OncePerRequestFilter {

    @Resource
    JwtUtils jwtUtils;

    /**
     * 该方法在每次请求时被调用，用于验证 JWT 并设置认证信息。
     *
     * @param request     HttpServletRequest 对象，包含请求信息。
     * @param response    HttpServletResponse 对象，包含响应信息。
     * @param filterChain 过滤器链，用于将请求传递给下一个过滤器或目标资源。
     * @throws ServletException 如果在处理请求过程中发生 Servlet 异常。
     * @throws IOException      如果在处理请求过程中发生 I/O 异常。
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 从请求头中获取 Authorization 字段
        String authorizationHeader = request.getHeader("Authorization");
        // 解码 JWT
        DecodedJWT jwt = jwtUtils.decode(authorizationHeader);
        // 如果 JWT 有效
        if (jwt != null) {
            // 将 JWT 转换为 UserDetails 对象
            UserDetails user = jwtUtils.toUser(jwt);
            // 创建一个 UsernamePasswordAuthenticationToken 对象，用于表示认证信息
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            // 设置认证信息的详细信息，例如请求的 IP 地址等
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // 将认证信息设置到 SecurityContextHolder 中，以便后续的过滤器或处理器可以使用
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            // 将用户 ID 存储在请求属性中，以便后续使用
            request.setAttribute("id", jwtUtils.toId(jwt));
        }
        // 将请求传递给下一个过滤器或目标资源
        filterChain.doFilter(request, response);
    }
}
