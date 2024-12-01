package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.Account;
import org.apache.ibatis.annotations.Mapper;

/**
 * 账户 Mapper 接口，继承自 MyBatis-Plus 的 BaseMapper，用于操作数据库中的账户表
 */
@Mapper
public interface AccountMapper extends BaseMapper<Account> {
}

