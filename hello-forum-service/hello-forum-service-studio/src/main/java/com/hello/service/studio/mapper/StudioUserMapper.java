package com.hello.service.studio.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hello.model.studio.pojos.StudioPost;
import com.hello.model.studio.pojos.StudioUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudioUserMapper extends BaseMapper<StudioUser> {


}
