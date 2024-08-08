package com.hello.service.post.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hello.common.constants.StudioPostMessageConstants;
import com.hello.model.post.pojos.PostConfig;
import com.hello.service.post.mapper.PostConfigMapper;
import com.hello.service.post.service.PostConfigService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PostConfigServiceImpl extends ServiceImpl<PostConfigMapper, PostConfig> implements PostConfigService {
    @Override
    public void updateByMap(Map map) {
        Object enable = map
                .get(StudioPostMessageConstants.ENABLE);
        Boolean isDown = true;
        if(enable.equals(1)){
            update(Wrappers.<PostConfig>lambdaUpdate()
                    .eq(PostConfig::getPostId,map.get(StudioPostMessageConstants.POST_ID))
                    .set(PostConfig::getDown,isDown));
        }

    }
}
