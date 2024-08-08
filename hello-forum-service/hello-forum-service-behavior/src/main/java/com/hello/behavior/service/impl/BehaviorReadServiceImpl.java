package com.hello.behavior.service.impl;

import com.alibaba.fastjson.JSON;
import com.hello.behavior.service.BehaviorReadService;
import com.hello.common.constants.BehaviorConstants;
import com.hello.common.constants.HotPostConstants;
import com.hello.common.context.UserThreadLocalUtil;
import com.hello.common.exception.BehaviorException;
import com.hello.model.behavior.dtos.BehaviorReadDTO;
import com.hello.model.mess.UpdatePostMess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.hello.common.constants.BehaviorConstants.PARAM_INVALID_EXCEPTION;
import static com.hello.common.constants.BehaviorConstants.READ_BEHAVIOR;

@Service
@Slf4j
public class BehaviorReadServiceImpl implements BehaviorReadService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Override
    public void read(BehaviorReadDTO behaviorReadDTO) {
        if (behaviorReadDTO == null || behaviorReadDTO.getPostId() == null) {
            throw new BehaviorException(PARAM_INVALID_EXCEPTION);
        }


        Integer currentUserId = UserThreadLocalUtil.getCurrentId();
        stringRedisTemplate.opsForHyperLogLog().add(READ_BEHAVIOR+behaviorReadDTO.getPostId().toString(),currentUserId.toString());
        UpdatePostMess updatePostMess = UpdatePostMess.builder()
                .postId(behaviorReadDTO.getPostId())
                .type(UpdatePostMess.UpdatePostType.VIEWS)
                .add(1)
                .build();
        kafkaTemplate.send(HotPostConstants.HOT_POST_SCORE_TOPIC, JSON.toJSONString(updatePostMess));

    }
}
