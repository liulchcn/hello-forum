package com.hello.service.post.controller.v1;


import com.hello.common.context.UserThreadLocalUtil;
import com.hello.common.result.Result;
import com.hello.model.post.dtos.PostLoadDTO;
import com.hello.model.post.pojos.Post;
import com.hello.model.post.vos.HotPostVO;
import com.hello.service.post.service.ArticleBloomFilterService;
import com.hello.service.post.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/article")
@Slf4j
public class PostLoadController {

    @Autowired
    private PostService postService;


    @PostMapping("/loadhome")
    public Result load(PostLoadDTO postLoadDTO) {

        List<HotPostVO>  hotPostList = postService.loadHomePage(postLoadDTO);

        return Result.success(hotPostList);
    }

    @PostMapping("/loadmore")
    public Result loadmore(PostLoadDTO postLoadDTO){
        return null;
    }


    @Autowired
    private ArticleBloomFilterService bloomFilterService;

    @GetMapping("/post")
    public String readArticle(@RequestParam Integer id) {
        log.info("加载id：{}",id);
        if (bloomFilterService.isArticleRead(Long.valueOf(UserThreadLocalUtil.getCurrentId()), Long.valueOf(id))) {
            return "您已阅读过这篇文章。";
        } else {
            // 模拟阅读文章逻辑
            bloomFilterService.markArticleAsRead(Long.valueOf(UserThreadLocalUtil.getCurrentId()), Long.valueOf(id));
            return "文章内容...";
        }
    }
}
