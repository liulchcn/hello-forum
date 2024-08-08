package com.hello.service.studio.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hello.model.studio.pojos.StudioMaterial;
import com.hello.service.studio.mapper.StudioMaterialMapper;
import org.springframework.web.multipart.MultipartFile;

public interface StudioMaterialService extends IService<StudioMaterial> {
    public String upload(MultipartFile multipartFile);
}
