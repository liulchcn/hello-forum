package com.hello.service.studio.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hello.model.studio.dtos.StudioPostDTO;
import com.hello.model.studio.pojos.StudioPost;

public interface StudioPostService extends IService<StudioPost> {

    /**
     *提交作品并发布
     * @param studioPostDTO
     */
    void submit(StudioPostDTO studioPostDTO);

    /**
     * 帖子上下架
     * @param dto
     */
    void downOrUp(StudioPostDTO dto);
}
