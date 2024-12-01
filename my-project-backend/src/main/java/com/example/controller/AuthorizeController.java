package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.request.ConfirmResetVO;
import com.example.entity.vo.request.EmailRegisterVO;
import com.example.entity.vo.request.EmailResetVO;
import com.example.service.AccountService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;
import java.util.function.Supplier;

@Validated
@RestController
@RequestMapping("/api/auth/")
public class AuthorizeController {

    @Resource
    AccountService service;

    /**
     * 处理发送验证码的请求
     *
     * @param email   接收验证码的邮箱
     * @param type    验证码类型，register 或 reset
     * @param request HttpServletRequest 对象
     * @return RestBean<Void> 响应对象
     */
    @GetMapping("/ask-code")
    public RestBean<Void> askVerifyCode(@RequestParam @Email String email,
                                        @RequestParam @Pattern(regexp = "(register|reset)") String type,
                                        HttpServletRequest request) {
        return this.messageHandle(() -> service.registerEmailVerifyCode(type, email, request.getRemoteAddr()));
    }

    /**
     * 处理用户注册请求
     *
     * @param vo 包含注册信息的 EmailRegisterVO 对象
     * @return RestBean<Void> 响应对象
     */
    @PostMapping("/register")
    public RestBean<Void> register(@RequestBody @Valid EmailRegisterVO vo) {
        return this.messageHandle(vo, service::registerEmailAccount);
    }

    /**
     * 处理消息的通用方法
     *
     * @param action Supplier 对象，用于获取处理结果
     * @return RestBean<Void> 响应对象
     */
    private RestBean<Void> messageHandle(Supplier<String> action) {
        String message = action.get();
        return message == null ? RestBean.success() : RestBean.failure(400, message);
    }

    /**
     * 处理重置密码确认请求
     *
     * @param vo 包含重置密码确认信息的 ConfirmResetVO 对象
     * @return RestBean<Void> 响应对象
     */
    @PostMapping("/reset-confirm")
    public RestBean<Void> resetConfirm(@RequestBody @Valid ConfirmResetVO vo) {
        return this.messageHandle(vo, service::resetConfirm);
    }

    /**
     * 处理重置密码请求
     *
     * @param vo 包含重置密码信息的 EmailResetVO 对象
     * @return RestBean<Void> 响应对象
     */
    @PostMapping("/reset-password")
    public RestBean<Void> resetConfirm(@RequestBody @Valid EmailResetVO vo) {
        return this.messageHandle(vo, service::resetEmailAccountPassword);
    }

    /**
     * 处理消息的通用方法，带参数
     *
     * @param vo       输入参数
     * @param function 处理函数
     * @return RestBean<Void> 响应对象
     */
    private <T> RestBean<Void> messageHandle(T vo, Function<T, String> function) {
        return messageHandle(() -> function.apply(vo));
    }
}
