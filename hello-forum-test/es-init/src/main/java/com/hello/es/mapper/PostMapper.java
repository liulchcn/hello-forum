package com.hello.es.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hello.es.pojo.SearchPostVO;
import com.hello.model.post.pojos.Post;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper extends BaseMapper<Post> {
    public List<SearchPostVO> loadPostList();
}
