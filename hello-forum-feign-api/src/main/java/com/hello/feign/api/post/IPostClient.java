package com.hello.feign.api.post;

import com.hello.common.result.Result;
import com.hello.model.post.dtos.PostDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "forum-post")
public interface IPostClient {
    @PostMapping("/api/v1/post/save")
    public Result savePost(@RequestBody PostDTO postDTO);
}
