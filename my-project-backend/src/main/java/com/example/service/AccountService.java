package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Account;
import com.example.entity.vo.request.ConfirmResetVO;
import com.example.entity.vo.request.EmailRegisterVO;
import com.example.entity.vo.request.EmailResetVO;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 账户服务接口，提供账户相关的操作，如注册、重置密码等
 */
public interface AccountService extends IService<Account>, UserDetailsService {

    /**
     * 根据用户名或邮箱查找账户
     *
     * @param text 用户名或邮箱
     * @return 账户对象
     */
    Account findAccountByNameOrEmail(String text);

    /**
     * 注册时发送验证码到邮箱
     *
     * @param type  验证码类型
     * @param email 邮箱地址
     * @param ip    用户IP地址
     * @return 验证码
     */
    String registerEmailVerifyCode(String type, String email, String ip);

    /**
     * 使用邮箱注册账户
     *
     * @param emailRegisterVO 邮箱注册信息
     * @return 注册结果
     */
    String registerEmailAccount(EmailRegisterVO emailRegisterVO);

    /**
     * 确认重置密码
     *
     * @param vo 确认重置密码信息
     * @return 确认结果
     */
    String resetConfirm(ConfirmResetVO vo);

    /**
     * 重置邮箱账户密码
     *
     * @param vo 重置密码信息
     * @return 重置结果
     */
    String resetEmailAccountPassword(EmailResetVO vo);

}
