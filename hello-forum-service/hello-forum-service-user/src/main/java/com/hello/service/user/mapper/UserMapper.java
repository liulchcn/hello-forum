package com.hello.service.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hello.model.user.pojos.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
