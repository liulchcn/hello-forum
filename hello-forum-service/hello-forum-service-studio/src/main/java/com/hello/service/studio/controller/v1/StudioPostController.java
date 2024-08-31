package com.hello.service.studio.controller.v1;

import com.hello.common.result.Result;
import com.hello.service.studio.idempotent.annoation.RepeatSubmit;
import com.hello.model.studio.dtos.StudioPostDTO;
import com.hello.service.studio.service.StudioPostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/post")
@Slf4j
public class StudioPostController {
    @Autowired
    private StudioPostService studioPostService;

    @PostMapping( "/submit")
    public Result submitPost(@ModelAttribute StudioPostDTO studioPostDTO){
        log.info("上传帖子:{}",studioPostDTO);
//        studioPostService.submit(studioPostDTO);
        return Result.success();
    }
    @PostMapping("/down_or_up")
    public Result downOrUp(@ModelAttribute StudioPostDTO dto){
        log.info("StudioPost-上架下架作品：{}",dto);
        studioPostService.downOrUp(dto);
        return Result.success();
    }
}
