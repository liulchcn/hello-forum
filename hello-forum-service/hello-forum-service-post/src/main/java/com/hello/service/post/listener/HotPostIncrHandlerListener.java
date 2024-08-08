package com.hello.service.post.listener;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hello.common.constants.HotPostConstants;
import com.hello.model.mess.PostVisitStreamMess;
import com.hello.service.post.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
@Slf4j
public class HotPostIncrHandlerListener {
    @Autowired
    private PostService postService;

    @KafkaListener(topics = HotPostConstants.HOT_POST_INCR_HANDLE_TOPIC)
    public void onMessage(String mess){
        if(StringUtils.isNotBlank(mess)){
            PostVisitStreamMess mess1 = JSON.parseObject(mess, PostVisitStreamMess.class);
            postService.updateScore(mess1);
        }
    }



}
