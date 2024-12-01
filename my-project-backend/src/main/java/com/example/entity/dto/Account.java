package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * 表示用户账户的实体类
 */
@Data
@TableName("db_account")
@AllArgsConstructor
public class Account {
    /**
     * 账户ID，类型为Integer，是数据库表的主键
     */
    @TableId(type = IdType.AUTO)
    Integer id;

    /**
     * 用户名，类型为String
     */
    String username;

    /**
     * 密码，类型为String
     */
    String password;

    /**
     * 电子邮件，类型为String
     */
    String email;

    /**
     * 角色，类型为String
     */
    String role;

    /**
     * 注册时间，类型为Date
     */
    Date registerTime;
}
