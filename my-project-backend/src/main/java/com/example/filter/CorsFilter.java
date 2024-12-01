package com.example.filter;

import com.example.utils.Const;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 处理跨域资源共享（CORS）的过滤器
 */
@Component
@Order(Const.ORDER_CORS)
public class CorsFilter extends HttpFilter {

    /**
     * 处理HTTP请求的过滤方法
     *
     * @param request  HTTP请求对象
     * @param response HTTP响应对象
     * @param chain    过滤器链
     * @throws IOException      如果在处理请求或响应时发生I/O错误
     * @throws ServletException 如果在处理请求或响应时发生Servlet错误
     */
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 添加CORS头部信息到响应中
        this.addCorsHeader(response, request);
        // 将请求和响应传递给过滤器链中的下一个过滤器或目标资源
        chain.doFilter(request, response);
    }

    /**
     * 添加CORS头部信息到HTTP响应中
     *
     * @param response HTTP响应对象
     * @param request  HTTP请求对象
     */
    private void addCorsHeader(HttpServletResponse response, HttpServletRequest request) {
        // 设置允许的源（Origin），这里使用请求头中的Origin
        response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        // 设置允许的HTTP方法
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        // 设置允许的HTTP头部
        response.addHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
    }
}
