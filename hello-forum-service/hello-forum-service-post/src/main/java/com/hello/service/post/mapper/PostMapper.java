package com.hello.service.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hello.model.post.dtos.PostLoadDTO;
import com.hello.model.post.pojos.Post;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper extends BaseMapper<Post> {
    List<Post> loadPostList(PostLoadDTO postLoadDTO);
}
