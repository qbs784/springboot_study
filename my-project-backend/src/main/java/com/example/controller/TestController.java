package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器，提供一个简单的测试接口
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    /**
     * 处理 GET 请求，返回一个简单的字符串 "hello"
     *
     * @return 字符串 "hello"
     */
    @GetMapping("/hello")
    public String test() {
        return "hello";
    }
}
