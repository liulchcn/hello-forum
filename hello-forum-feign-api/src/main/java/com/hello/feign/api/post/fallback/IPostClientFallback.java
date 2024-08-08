package com.hello.feign.api.post.fallback;

import com.hello.common.result.Result;
import com.hello.feign.api.post.IPostClient;
import com.hello.model.post.dtos.PostDTO;
import com.hello.model.studio.constants.StudioPostConstants;

public class IPostClientFallback implements IPostClient {
    @Override
    public Result savePost(PostDTO postDTO) {
        return Result.error(StudioPostConstants.POST_SAVED_FAILED);
    }
}
