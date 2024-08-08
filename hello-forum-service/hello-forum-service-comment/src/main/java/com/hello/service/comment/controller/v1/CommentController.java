package com.hello.service.comment.controller.v1;

import com.hello.common.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {

    @GetMapping("/check")
    public Result check(){
        return Result.success();
    }
}
