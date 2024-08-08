package com.hello.service.studio.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hello.common.constants.UploadFileConstans;
import com.hello.common.context.UserThreadLocalUtil;
import com.hello.common.exception.UploadFileException;
import com.hello.fileStarter.service.FileStorageService;
import com.hello.model.studio.pojos.StudioMaterial;
import com.hello.service.studio.mapper.StudioMaterialMapper;
import com.hello.service.studio.service.StudioMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class StudioMaterialServiceImpl extends ServiceImpl<StudioMaterialMapper, StudioMaterial> implements StudioMaterialService {
    /**
     *
     */
    @Autowired
    private FileStorageService fileStorageService;
    @Override
    public String upload(MultipartFile multipartFile) {
        log.info("文件中的内容：{}",multipartFile);
        if(multipartFile==null||multipartFile.getSize()==0){
            throw  new UploadFileException(UploadFileConstans.UPLOAD_FILE_NULL_ERROR);
        }
        //2.上传图片到minIO中
        String fileName = UUID.randomUUID().toString().replace("-", "");
        //aa.jpg
        String originalFilename = multipartFile.getOriginalFilename();
        String postfix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileUrl = null;
        log.info("准备上传图片到minio中。。。");

        
        return fileUrl;
    }
}
