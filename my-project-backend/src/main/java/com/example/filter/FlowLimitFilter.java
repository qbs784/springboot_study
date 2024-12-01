package com.example.filter;

import com.example.entity.RestBean;
import com.example.utils.Const;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@Order(Const.ORDER_LIMIT)
public class FlowLimitFilter extends HttpFilter {

    @Resource
    StringRedisTemplate template;

    /**
     * 处理 HTTP 请求的过滤方法
     *
     * @param request  HTTP 请求对象
     * @param response HTTP 响应对象
     * @param chain    过滤器链
     * @throws IOException      如果在处理请求或响应时发生 I/O 错误
     * @throws ServletException 如果在处理请求时发生 Servlet 错误
     */
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 获取客户端的 IP 地址
        String address = request.getRemoteAddr();
        // 检查是否超过限流阈值
        if (!tryCount(address))
            // 如果超过限流阈值，向客户端返回错误信息
            this.writeBlockMessage(response);
        else
            // 如果未超过限流阈值，继续处理请求
            chain.doFilter(request, response);
    }

    /**
     * 尝试增加访问计数，并检查是否超过限流阈值
     *
     * @param address 客户端的 IP 地址
     * @return 如果未超过限流阈值，返回 true；否则返回 false
     */
    private boolean tryCount(String address) {
        // 对 IP 地址进行同步处理，确保线程安全
        synchronized (address.intern()) {
            // 检查是否已经被限流
            if (Boolean.TRUE.equals(template.hasKey(Const.FLOW_LIMIT_BLOCK + address)))
                return false;
            // 检查是否在限流周期内
            return this.limitPeriodCheck(address);
        }
    }

    /**
     * 向客户端返回被限流的错误信息
     *
     * @param response HTTP 响应对象
     * @throws IOException 如果在写入响应时发生 I/O 错误
     */
    private void writeBlockMessage(HttpServletResponse response) throws IOException {
        // 设置 HTTP 响应状态码为 403，表示禁止访问
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        // 设置响应的内容类型为 JSON，并指定字符编码为 UTF-8
        response.setContentType("application/json;charset=utf-8");
        // 获取响应的输出流
        PrintWriter writer = response.getWriter();
        // 向客户端返回错误信息
        writer.write(RestBean.forbidden("操作频繁，请稍后再试").asJson());
    }

    /**
     * 检查在指定周期内的访问次数是否超过限流阈值
     *
     * @param ip 客户端的 IP 地址
     * @return 如果未超过限流阈值，返回 true；否则返回 false
     */
    private boolean limitPeriodCheck(String ip) {
        // 检查计数器是否存在
        if (Boolean.TRUE.equals(template.hasKey(Const.FLOW_LIMIT_COUNTER + ip))) {
            // 获取计数器的值，并增加 1
            long increment = Optional.ofNullable(template.opsForValue().increment(Const.FLOW_LIMIT_COUNTER + ip)).orElse(0L);
            // 如果计数器的值超过 10，则进行限流处理
            if (increment > 10) {
                // 设置限流标志，并设置过期时间为 30 秒
                template.opsForValue().set(Const.FLOW_LIMIT_BLOCK + ip, "", 30, TimeUnit.SECONDS);
                return false;
            }
        } else {
            // 如果计数器不存在，则创建计数器，并设置过期时间为 3 秒
            template.opsForValue().set(Const.FLOW_LIMIT_COUNTER + ip, "1", 3, TimeUnit.SECONDS);
        }
        return true;
    }
}
