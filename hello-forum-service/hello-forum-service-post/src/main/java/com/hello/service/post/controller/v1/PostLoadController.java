package com.hello.service.post.controller.v1;


import com.hello.common.result.Result;
import com.hello.model.post.dtos.PostLoadDTO;
import com.hello.model.post.vos.HotPostVO;
import com.hello.service.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/article")
public class PostLoadController {

    @Autowired
    private PostService postService;


    @PostMapping("/loadhome")
    public Result load(PostLoadDTO postLoadDTO) {

        List<HotPostVO>  HotPostList = postService.loadHomePage(postLoadDTO);

        return Result.success();

    }
    @GetMapping("/get")
    public Result get(){
        return Result.success("ok");
    }
}
