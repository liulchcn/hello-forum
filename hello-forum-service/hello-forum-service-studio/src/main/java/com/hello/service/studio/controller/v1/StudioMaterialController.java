package com.hello.service.studio.controller.v1;

import com.hello.common.result.Result;
//import com.hello.service.studio.service.StudioMaterialService;
import com.hello.model.studio.dtos.UploadPartDTO;
import com.hello.service.studio.service.StudioMaterialService;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/material")
@Slf4j
public class StudioMaterialController {

    @Autowired
    private StudioMaterialService studioMaterialService;

    @PostMapping("/upload/pic")
    public Result uploadPic(@ModelAttribute MultipartFile multipartFile){
        log.info("上传文件：{}",multipartFile);
        String fileUrl = studioMaterialService.uploadPic(multipartFile);
        return Result.success(fileUrl);
    }


    @PostMapping("/uploadPart")
    public Result<List<String>> uploadPart(@ModelAttribute UploadPartDTO uploadPartRequest) throws EncoderException, IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return studioMaterialService.uploadPart(uploadPartRequest);
    }
}
