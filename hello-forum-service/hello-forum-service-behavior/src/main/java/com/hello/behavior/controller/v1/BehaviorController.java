package com.hello.behavior.controller.v1;

import com.hello.behavior.service.BehaviorLikesService;
import com.hello.behavior.service.BehaviorReadService;
import com.hello.behavior.service.BehaviorUnlikesService;
import com.hello.common.result.Result;
import com.hello.model.behavior.dtos.BehaviorReadDTO;
import com.hello.model.behavior.dtos.BehaviorLikesPostDTO;
import com.hello.model.behavior.dtos.BehaviorUnlikesPostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/behavior")
public class BehaviorController {
    @Autowired
    private BehaviorLikesService behaviorLikesService;
    @Autowired
    private BehaviorUnlikesService behaviorUnlikesService;
    @Autowired
    private BehaviorReadService behaviorReadService;

    @PostMapping("/like")
    public Result like(@RequestBody BehaviorLikesPostDTO behaviorLikesPostDTO){
        behaviorLikesService.like(behaviorLikesPostDTO);
        return Result.success();
    }

    @PostMapping("/unlike")
    public Result unlike(@RequestBody BehaviorUnlikesPostDTO behaviorLikesPostDTO){
        behaviorUnlikesService.unlike(behaviorLikesPostDTO);
        return Result.success();
    }

    @PostMapping("/read")
    public Result read(@RequestBody BehaviorReadDTO behaviorReadDTO){
        behaviorReadService.read(behaviorReadDTO);
        return Result.success();
    }
}
