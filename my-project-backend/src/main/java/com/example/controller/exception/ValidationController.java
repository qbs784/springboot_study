package com.example.controller.exception;

import com.example.entity.RestBean;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器，用于处理验证异常
 */
@Slf4j
@RestControllerAdvice
public class ValidationController {

    /**
     * 处理验证异常
     *
     * @param e 捕获的验证异常
     * @return 返回一个包含错误信息的响应实体
     */
    @ExceptionHandler(ValidationException.class)
    public RestBean<Void> validationException(ValidationException e) {
        // 记录警告信息
        log.warn("Resolve [{}: {}]", e.getClass().getName(), e.getMessage());
        // 返回一个失败的响应实体，状态码为 400，错误信息为 "请求参数有误"
        return RestBean.failure(400, "请求参数有误");
    }
}
