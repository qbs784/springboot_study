package com.example.entity.vo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 表示通过电子邮件注册的用户信息的类。
 * 该类包含用于验证电子邮件地址、验证码、用户名和密码的字段。
 */
@Data
public class    EmailRegisterVO {
    /**
     * 用户的电子邮件地址。
     * 该字段必须是有效的电子邮件地址，并且长度至少为 4 个字符。
     */
    @Email
    @Length(min = 4)
    String email;

    /**
     * 用于验证用户电子邮件地址的验证码。
     * 该字段的长度必须为 6 个字符。
     */
    @Length(max = 6, min = 6)
    String code;

    /**
     * 用户的用户名。
     * 该字段可以包含字母、数字、汉字，长度在 1 到 10 个字符之间。
     */
    @Pattern(regexp = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$")
    @Length(min = 1, max = 10)
    String username;

    /**
     * 用户的密码。
     * 该字段的长度必须在 6 到 20 个字符之间。
     */
    @Length(min = 6, max = 20)
    String password;
}
