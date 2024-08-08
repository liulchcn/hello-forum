package com.hello.behavior.service.impl;

import com.alibaba.fastjson.JSON;
import com.hello.behavior.service.BehaviorLikesService;
import com.hello.common.constants.HotPostConstants;
import com.hello.common.context.UserThreadLocalUtil;
import com.hello.common.exception.BehaviorException;
import com.hello.model.behavior.dtos.BehaviorLikesPostDTO;
import com.hello.model.mess.UpdatePostMess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.hello.common.constants.BehaviorConstants.*;

@Service
@Slf4j
public class BehaviorLikesServiceImpl implements BehaviorLikesService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    @Override
    public void like(BehaviorLikesPostDTO behaviorLikesPostDTO) {
        if(behaviorLikesPostDTO == null || behaviorLikesPostDTO.getPostId() ==null || checkParam(behaviorLikesPostDTO)){
            throw new BehaviorException(PARAM_INVALID_EXCEPTION);
        }
        Integer currentUserId = UserThreadLocalUtil.getCurrentId();

        UpdatePostMess updatePostMess = UpdatePostMess.builder()
                .postId(behaviorLikesPostDTO.getPostId())
                .type(UpdatePostMess.UpdatePostType.LIKES)
                .build();
        Object object = stringRedisTemplate.opsForHash().get(LIKE_BEHAVIOR + behaviorLikesPostDTO.getPostId().toString(), currentUserId.toString());
        if(behaviorLikesPostDTO.getOperation()== OPERATION_CANCEL_LIKE){
            if(object!=null){
                log.info("保存当前点赞信息,key：{},",behaviorLikesPostDTO);
                stringRedisTemplate.opsForHash().put(LIKE_BEHAVIOR + behaviorLikesPostDTO.getPostId().toString(),currentUserId.toString(), JSON.toJSONString(behaviorLikesPostDTO));
                updatePostMess.setAdd(1);
            }else{
                throw new BehaviorException(DUPILICAT_LIKE);
            }
        }else {
            if(object!=null){
                log.info("删除当前的key：{}",behaviorLikesPostDTO);
                stringRedisTemplate.opsForHash().delete(LIKE_BEHAVIOR + behaviorLikesPostDTO.getPostId().toString(), currentUserId.toString());
                updatePostMess.setAdd(-1);
            }
        }
        kafkaTemplate.send(HotPostConstants.HOT_POST_SCORE_TOPIC,JSON.toJSONString(updatePostMess));

    }


    private boolean checkParam(BehaviorLikesPostDTO behaviorLikesPostDTO) {
        short type = behaviorLikesPostDTO.getType();
        short operation = behaviorLikesPostDTO.getOperation();
        if(type != LIKE_TYPE_COMMENT && type != LIKE_TYPE_POST){
            return false;
        }
        if(operation != OPERATION_LIKE && operation!= OPERATION_CANCEL_LIKE
        && operation!=OPERATION_CANCEL_UNLIKE && operation!= OPERATION_UNLIKE
        ){
            return false;
        }

        return true;
    }

}
