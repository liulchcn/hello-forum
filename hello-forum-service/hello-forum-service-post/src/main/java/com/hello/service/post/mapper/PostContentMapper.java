package com.hello.service.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hello.model.post.pojos.PostContent;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostContentMapper extends BaseMapper<PostContent> {
}
