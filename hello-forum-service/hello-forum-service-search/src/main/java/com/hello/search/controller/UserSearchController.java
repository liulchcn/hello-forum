package com.hello.search.controller;

import com.hello.common.result.Result;
import com.hello.search.service.PostSearchService;
import com.hello.search.vos.HistorySearchDTO;
import com.hello.search.vos.UserSearchRecordsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/post/history")
@Slf4j
public class UserSearchController {
    @Autowired
    private PostSearchService postSearchService;
    @PostMapping("/load")
    public Result findUserSearchRecords(){
        UserSearchRecordsVO userSearchRecordsVO = postSearchService.findSearchRecords();
        return Result.success(userSearchRecordsVO);
    }
    @PostMapping("/delete")
    public Result delUserSearch(@ModelAttribute HistorySearchDTO historySearchDTO){
        postSearchService.delete(historySearchDTO);
        return Result.success();
    }
}
