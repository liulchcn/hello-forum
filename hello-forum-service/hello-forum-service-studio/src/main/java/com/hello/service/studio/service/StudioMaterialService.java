package com.hello.service.studio.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hello.common.result.Result;
import com.hello.model.studio.dtos.UploadVideoDTO;
import com.hello.model.studio.pojos.StudioMaterial;
import com.hello.model.studio.dtos.UploadPartDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface StudioMaterialService extends IService<StudioMaterial> {

    /**
     * 上传图片
     * @param multipartFile
     * @return fileUrl
     */
    public String uploadPic(MultipartFile multipartFile);

    /**
     * 上传完整的作品
     * @param uploadVideoDTO
     */
    public void uploadTotal(UploadVideoDTO uploadVideoDTO);

    /**
     * 上传
     * @param uploadPartRequest
     * @return
     * @throws IOException
     */
    Result<List<String>> uploadPart(UploadPartDTO uploadPartRequest) throws IOException;
}
