package com.hello.search.controller;


import com.hello.common.result.Result;
import com.hello.search.pojos.AssociateWords;
import com.hello.model.search.dtos.UserSearchDTO;
import com.hello.model.search.vos.PostSearchVO;
import com.hello.search.service.PostSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/post/search")
@Slf4j
public class PostSearchController {
    @Autowired
    private PostSearchService postSearchService;
    @PostMapping("/search")
    public Result search(@RequestBody UserSearchDTO userSearchDTO) throws IOException {
        log.info("搜索：{}",userSearchDTO);
        PostSearchVO postSearchVO = postSearchService.search(userSearchDTO);
        return Result.success(postSearchVO);
    }

    @PostMapping("/associate")
    public Result load(@RequestBody UserSearchDTO userSearchDto) {
        List<AssociateWords>result =  postSearchService.findAssociate(userSearchDto);
        return Result.success(result);
    }
}
