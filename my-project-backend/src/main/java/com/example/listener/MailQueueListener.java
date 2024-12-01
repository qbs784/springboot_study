package com.example.listener;

import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "email")
public class MailQueueListener {

    @Resource
    JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    String mailUsername;

    @RabbitHandler
    public void sendMailMessage(Map<String, Object> data) {
        // 从数据中获取邮件地址
        String email = (String) data.get("email");
        // 从数据中获取验证码
        Integer code = (Integer) data.get("code");
        // 从数据中获取邮件类型
        String type = (String) data.get("type");
        // 根据邮件类型创建邮件内容
        SimpleMailMessage message = switch (type) {
            case "register" -> createMessage("欢迎注册", "您的验证码：" + code + "，有效时间3分钟！", email);
            case "reset" -> createMessage("重置密码", "您的验证码：" + code + "，有效时间3分钟！", email);
            default -> null;
        };
        // 如果邮件内容为空，则不发送邮件
        if (message == null) return;
        // 发送邮件
        mailSender.send(message);
    }

    private SimpleMailMessage createMessage(String title, String content, String email) {
        // 创建一个简单的邮件消息
        SimpleMailMessage message = new SimpleMailMessage();
        // 设置邮件的收件人
        message.setTo(email);
        // 设置邮件的主题
        message.setSubject(title);
        // 设置邮件的内容
        message.setText(content);
        // 设置邮件的发件人
        message.setFrom(mailUsername);
        // 返回创建的邮件消息
        return message;

    }
}
