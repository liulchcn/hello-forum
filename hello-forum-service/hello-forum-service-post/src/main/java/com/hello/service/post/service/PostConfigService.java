package com.hello.service.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hello.model.post.pojos.PostConfig;

import java.util.Map;

public interface PostConfigService extends IService<PostConfig> {
     public void updateByMap(Map map);
}
