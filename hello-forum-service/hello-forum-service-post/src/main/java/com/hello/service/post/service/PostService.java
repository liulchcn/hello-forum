package com.hello.service.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hello.common.result.Result;
import com.hello.model.mess.PostVisitStreamMess;
import com.hello.model.post.dtos.PostDTO;
import com.hello.model.post.dtos.PostLoadDTO;
import com.hello.model.post.pojos.Post;
import com.hello.model.post.vos.HotPostVO;

import java.util.List;

public interface PostService extends IService<Post> {

    Result savePost(PostDTO postDTO);

    public void updateScore(PostVisitStreamMess postVisitStreamMess);

    List<HotPostVO> loadHomePage(PostLoadDTO postLoadDTO);
}
