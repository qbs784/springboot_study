package com.example.entity.vo.request;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 表示重置密码时的确认请求的类。
 * 该类包含一个电子邮件地址和一个验证码，用于验证用户身份并重置密码。
 */
@Data
public class ConfirmResetVO {
    /**
     * 用户的电子邮件地址，用于接收重置密码的链接或验证码。
     * 该字段必须是一个有效的电子邮件地址。
     */
    @Email
    String email;

    /**
     * 重置密码时使用的验证码。
     * 该字段必须是一个长度为6的字符串。
     */
    @Length(min=6,max=6)
    String code;

    /**
     * 创建一个新的 ConfirmResetVO 对象。
     *
     * @param email 用户的电子邮件地址。
     * @param code 重置密码时使用的验证码。
     */
    public ConfirmResetVO(@Email String email, @Length(min=6,max=6) String code) {
        this.email = email;
        this.code = code;
    }
}
