package com.hello.service.post.listener;

import com.alibaba.fastjson.JSON;
import com.hello.common.constants.StudioPostMessageConstants;
import com.hello.service.post.service.PostConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class PostIsDownListenner {
    @Autowired
    private PostConfigService postConfigService;

    @KafkaListener(topics = StudioPostMessageConstants.POST_UP_OR_DOWN_TOPIC)
    public void onMessage(String message){
        if(StringUtils.isNotBlank(message)){
            Map map = JSON.parseObject(message, Map.class);
            postConfigService.updateByMap(map);
        }
    }
}
