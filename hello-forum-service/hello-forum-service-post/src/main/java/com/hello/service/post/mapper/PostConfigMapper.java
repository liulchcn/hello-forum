package com.hello.service.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hello.model.post.pojos.Post;
import com.hello.model.post.pojos.PostConfig;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostConfigMapper extends BaseMapper<PostConfig> {
}
