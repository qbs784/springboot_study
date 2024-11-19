package com.example.filter;

import com.example.entity.RestBean;
import com.example.utils.FlowUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import com.example.utils.Const;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@Order(Const.ORDER_LIMIT)
public class FlowLimitFilter extends HttpFilter {

    @Resource
    StringRedisTemplate template;


    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String address = request.getRemoteAddr();
        if (!tryCount(address))
            this.writeBlockMessage(response);
        else
            chain.doFilter(request, response);
    }

    private boolean tryCount(String address) {
        synchronized (address.intern()) {
            if(Boolean.TRUE.equals(template.hasKey(Const.FLOW_LIMIT_BLOCK + address)))
                return false;
            return this.limitPeriodCheck(address);
        }
    }

    private void writeBlockMessage(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(RestBean.forbidden("操作频繁，请稍后再试").asJson());
    }

    private boolean limitPeriodCheck(String ip) {
        if(Boolean.TRUE.equals(template.hasKey(Const.FLOW_LIMIT_COUNTER + ip))){
            long increment = Optional.ofNullable(template.opsForValue().increment(Const.FLOW_LIMIT_COUNTER+ip)).orElse(0L);
            if(increment>10){
                template.opsForValue().set(Const.FLOW_LIMIT_BLOCK+ip,"",30,TimeUnit.SECONDS);
                return false;
            }
        }else{
            template.opsForValue().set(Const.FLOW_LIMIT_COUNTER+ip, "1",3, TimeUnit.SECONDS);
        }
        return true;
    }
}
