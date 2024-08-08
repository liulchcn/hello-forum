package com.hello.service.studio.controller.v1;

import com.hello.common.result.Result;
//import com.hello.service.studio.service.StudioMaterialService;
import com.hello.service.studio.service.StudioMaterialService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/material")
@Slf4j
public class StudioMaterialController {

    @Autowired
    private StudioMaterialService studioMaterialService;

    @PostMapping("/upload")
    public Result uploadMaterial(MultipartFile multipartFile){
        log.info("上传文件：{}",multipartFile);
        String fileUrl = studioMaterialService.upload(multipartFile);
        return Result.success(fileUrl);
    }


}
