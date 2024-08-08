package com.hello.service.post.feign;

import com.hello.common.result.Result;
import com.hello.feign.api.post.IPostClient;
import com.hello.model.post.dtos.PostDTO;
import com.hello.service.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostClient implements IPostClient {
    @Autowired
    private PostService postService;
    @Override
    public Result savePost(@RequestBody PostDTO postDTO) {
        Result result = postService.savePost(postDTO);
        return result;
    }
}
