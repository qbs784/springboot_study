package com.example.config;

import com.example.entity.RestBean;
import com.example.entity.dto.Account;
import com.example.entity.vo.response.AuthorizeVO;
import com.example.filter.JwtAuthorizeFilter;
import com.example.service.AccountService;
import com.example.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Spring Security 配置类
 */
@Configuration
public class SecurityConfiguration {

    @Resource
    JwtUtils jwtUtils;

    @Resource
    JwtAuthorizeFilter filter;

    @Resource
    AccountService service;

    /**
     * 配置 SecurityFilterChain
     *
     * @param http HttpSecurity 对象
     * @return SecurityFilterChain 对象
     */
    @SneakyThrows
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        return http
                .authorizeHttpRequests(conf -> conf
                        .requestMatchers("/api/auth/**", "/error").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(conf -> conf
                        .loginProcessingUrl("/api/auth/login")
                        .failureHandler(this::onAuthenticationFailure)
                        .successHandler(this::onAuthenticationSuccess)
                )
                .logout(conf -> conf
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(this::onLogoutSuccess)
                )
                .exceptionHandling(conf -> conf
                        .authenticationEntryPoint(this::onUnauthorized)
                        .accessDeniedHandler(this::onAccessDeny)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(conf -> conf
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * 处理访问被拒绝的情况
     *
     * @param httpServletRequest  HttpServletRequest 对象
     * @param httpServletResponse HttpServletResponse 对象
     * @param e                   AccessDeniedException 异常
     * @throws IOException 可能抛出的 IO 异常
     */
    private void onAccessDeny(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(RestBean.unauthorized(e.getMessage()).asJson());
    }

    /**
     * 处理未授权的情况
     *
     * @param request       HttpServletRequest 对象
     * @param response      HttpServletResponse 对象
     * @param authException AuthenticationException 异常
     * @throws IOException 可能抛出的 IO 异常
     */
    private void onUnauthorized(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(RestBean.unauthorized(authException.getMessage()).asJson());
    }

    /**
     * 处理认证失败的情况
     *
     * @param request  HttpServletRequest 对象
     * @param response HttpServletResponse 对象
     * @param e        AuthenticationException 异常
     * @throws IOException 可能抛出的 IO 异常
     */
    private void onAuthenticationFailure(HttpServletRequest request,
                                         HttpServletResponse response,
                                         AuthenticationException e) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(RestBean.unauthorized(e.getMessage()).asJson());
    }

    /**
     * 处理认证成功的情况
     *
     * @param request  HttpServletRequest 对象
     * @param response HttpServletResponse 对象
     * @param auth     Authentication 对象
     * @throws IOException 可能抛出的 IO 异常
     */
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication auth) throws IOException {
        User user = (User) auth.getPrincipal();
        Account account = service.findAccountByNameOrEmail(user.getUsername());
        String token = jwtUtils.generateJWT(user, account.getId(), account.getUsername());
        AuthorizeVO vo = new AuthorizeVO();
        vo.setExpire(jwtUtils.expireTime());
        vo.setToken(token);
        vo.setRole(account.getRole());
        vo.setUsername(account.getUsername());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(RestBean.success(vo).asJson());
    }

    /**
     * 处理注销成功的情况
     *
     * @param request  HttpServletRequest 对象
     * @param response HttpServletResponse 对象
     * @param auth     Authentication 对象
     * @throws IOException 可能抛出的 IO 异常
     */
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication auth) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String authorization = request.getHeader("Authorization");
        if (jwtUtils.invalidateJwt(authorization)) {
            out.write(RestBean.success().asJson());
        } else {
            out.write(RestBean.failure(400, "退出失败！").asJson());
        }
    }
}
