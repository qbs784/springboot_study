package com.example.entity.vo.request;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 表示通过电子邮件重置密码的请求对象。
 * 该对象包含用于验证和重置密码的必要字段。
 */
@Data
public class EmailResetVO {
    /**
     * 用户的电子邮件地址，用于接收重置密码的链接或验证码。
     * 该字段必须是有效的电子邮件格式。
     */
    @Email
    String email;

    /**
     * 用户设置的新密码。
     * 该字段必须在6到20个字符之间。
     */
    @Length(min = 6, max = 20)
    String password;

    /**
     * 用于验证重置密码请求的验证码。
     * 该字段必须是6个字符的长度。
     */
    @Length(min = 6, max = 6)
    String code;
}
