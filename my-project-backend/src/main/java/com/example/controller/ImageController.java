package com.example.controller;

import com.example.entity.RestBean;
import com.example.service.ImageService;
import com.example.utils.Const;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/image")
public class ImageController {

    @Resource
    ImageService imageService;

    @PostMapping("/avatar")
    public RestBean<String> uploadAvatar(@RequestParam("file") MultipartFile file,
                                         @RequestAttribute(Const.ATTR_USER_ID) int id) throws IOException {
        if(file.getSize()>1025*100)
            return RestBean.failure(400,"头像大小不得超过100KB");
        log.info("正在进行上传");
        String url = imageService.uploadAvatar(file, id);
        if(url!=null){
            log.info("头像上传成功，大小："+file.getSize());
            return RestBean.success(url);
        }else{
            return RestBean.failure(400,"头像上传失败");
        }
    }
}
