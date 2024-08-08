package com.hello.behavior.service.impl;

import com.alibaba.fastjson.JSON;
import com.hello.behavior.service.BehaviorLikesService;
import com.hello.behavior.service.BehaviorUnlikesService;
import com.hello.common.constants.HotPostConstants;
import com.hello.common.context.UserThreadLocalUtil;
import com.hello.common.exception.BehaviorException;
import com.hello.model.behavior.dtos.BehaviorLikesPostDTO;
import com.hello.model.behavior.dtos.BehaviorUnlikesPostDTO;
import com.hello.model.mess.UpdatePostMess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.hello.common.constants.BehaviorConstants.*;

@Service
@Slf4j
public class BehavioryUnlikesServiceImpl implements BehaviorUnlikesService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;


    private boolean checkParam(BehaviorUnlikesPostDTO behaviorUnlikesPostDTO) {
        short type = behaviorUnlikesPostDTO.getType();
        short operation = behaviorUnlikesPostDTO.getOperation();
        if(type != LIKE_TYPE_COMMENT && type != LIKE_TYPE_POST){
            return false;
        }
        if(operation!=OPERATION_CANCEL_UNLIKE && operation!= OPERATION_UNLIKE){
            return false;
        }

        return true;
    }

    @Override
    public void unlike(BehaviorUnlikesPostDTO behaviorUnlikesPostDTO) {
        if(behaviorUnlikesPostDTO == null || behaviorUnlikesPostDTO.getPostId() ==null || checkParam(behaviorUnlikesPostDTO)){
            throw new BehaviorException(PARAM_INVALID_EXCEPTION);
        }
        Integer currentUserId = UserThreadLocalUtil.getCurrentId();

        UpdatePostMess updatePostMess = UpdatePostMess.builder()
                .postId(behaviorUnlikesPostDTO.getPostId())
                .type(UpdatePostMess.UpdatePostType.LIKES)
                .build();
        Object object = stringRedisTemplate.opsForHash().get(UN_LIKE_BEHAVIOR + behaviorUnlikesPostDTO.getPostId().toString(), currentUserId.toString());
        if(behaviorUnlikesPostDTO.getOperation()== OPERATION_CANCEL_UNLIKE){
            if(object!=null){
                log.info("保存当前点赞信息,key：{},",behaviorUnlikesPostDTO);
                stringRedisTemplate.opsForHash().put(UN_LIKE_BEHAVIOR + behaviorUnlikesPostDTO.getPostId().toString(),currentUserId.toString(), JSON.toJSONString(behaviorUnlikesPostDTO));
                updatePostMess.setAdd(1);
            }else{
                throw new BehaviorException(DUPILICAT_UNLIKE);
            }
        }else {
            if(object!=null){
                log.info("删除当前的key：{}",behaviorUnlikesPostDTO);
                stringRedisTemplate.opsForHash().delete(UN_LIKE_BEHAVIOR + behaviorUnlikesPostDTO.getPostId().toString(), currentUserId.toString());
                updatePostMess.setAdd(-1);
            }
        }
        kafkaTemplate.send(HotPostConstants.HOT_POST_SCORE_TOPIC,JSON.toJSONString(updatePostMess));
    }
}
