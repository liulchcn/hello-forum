package com.hello.service.studio.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.hello.common.exception.StudioPostScanException;
import com.hello.common.result.Result;
import com.hello.feign.api.post.IPostClient;
import com.hello.model.enums.AppHttpCodeEnum;
import com.hello.model.post.dtos.PostDTO;
import com.hello.model.studio.constants.StudioPostConstants;
import com.hello.model.studio.pojos.StudioChannel;
import com.hello.model.studio.pojos.StudioPost;
import com.hello.model.studio.pojos.StudioUser;
import com.hello.service.studio.mapper.StudioChannelMapper;
import com.hello.service.studio.mapper.StudioPostMapper;
import com.hello.service.studio.mapper.StudioUserMapper;
import com.hello.service.studio.service.StudioPostScanService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.hello.model.studio.constants.StudioPostConstants.*;
import static jodd.util.ThreadUtil.sleep;

@Service
@Slf4j
@Transactional
public class StudioPostScanServiceImpl implements StudioPostScanService {
    @Autowired
    private StudioPostMapper studioPostsMapper;
    @Autowired
    private StudioChannelMapper studioChannelMapper;
    @Autowired
    private StudioUserMapper studioUserMapper;
    @Autowired
    private IPostClient postClient;

    @Override
    @Async
    public void autoScanPosts(Integer id) {
        sleep(100);
        log.info("将要被扫描的帖子的id：{}",id);
        StudioPost studioPost = studioPostsMapper.selectById(id);
        if(studioPost==null){
            throw new StudioPostScanException(StudioPostConstants.POST_TO_BE_SCANNED_NOT_FOUND);
        }
        if(studioPost.getStatus().equals(StudioPost.Status.SUBMIT.getCode())){
            Map<String, Object> textAndImages = handleTextAndImages(studioPost);
        }
        /**
         * TODO 设置敏感词等等的自动审核功能；
         */

        Result result = savePost(studioPost);

        if (result.getCode()!=200){
            throw new StudioPostScanException(POST_SAVED_FAILED);
        }

        log.info("保存帖子最后得到帖子id为：{}",result.getData());
        studioPost.setPostId((Long) result.getData());

        updatePost(studioPost,POST_SCANNED_SUCCESS_STATUS,SCAN_POST_SUCCESS);
    }

    private void updatePost(StudioPost studioPost, Short status, String scanPostSuccess) {
        studioPost.setStatus(status);
        studioPost.setReason(scanPostSuccess);
        studioPostsMapper.updateById(studioPost);
    }

    private Result savePost(StudioPost studioPost) {
        PostDTO postDTO = new PostDTO();
        BeanUtils.copyProperties(studioPost,postDTO);
        StudioChannel channel = studioChannelMapper.selectById(studioPost.getChannelId());
        if(channel!=null){
            postDTO.setChannelName(channel.getName());
        }
        StudioUser studioUser = new StudioUser();
        if (studioPost.getUserId()!=null){
           studioUser = studioUserMapper.selectById(studioPost.getUserId());
        }

        if(studioUser!=null){
            postDTO.setBloggerName(studioUser.getUserName());
        }
        if(studioPost.getPostId()!=null){
            postDTO.setId(studioPost.getPostId());
        }
        postDTO.setId(null);
        postDTO.setCreatedTime( new Date());
        Result result = postClient.savePost(postDTO);
        return result;
    }

    private Map<String, Object> handleTextAndImages(StudioPost studioPost) {
        StringBuilder stringBuilder = new StringBuilder();

        List<String> images = new ArrayList<>();

        if (StringUtils.isNotBlank(studioPost.getContent())) {
            List<Map> maps = JSONArray.parseArray(studioPost.getContent(), Map.class);
            for (Map map : maps) {
                if(map.get(FRONT_END_PASS_JSON_TYPE).equals(FRONT_END_PASS_JSON__TEXT)){
                    stringBuilder.append(map.get(FRONT_END_PASS_JSON__VALUE));
                }
                if(map.get(FRONT_END_PASS_JSON_TYPE).equals(FRONT_END_PASS_JSON_IMAGE)){
                    images.add((String) map.get(FRONT_END_PASS_JSON__VALUE));
                }
            }
        }
        if (StringUtils.isNotBlank(studioPost.getImages())){
            String[] split = studioPost.getImages().split(SEPARATOR);
            images.addAll(Arrays.asList(split));
        }
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put(STUDIO_POST_CONTENT,stringBuilder.toString());
        resultMap.put(STUDIO_POST_IMAGES,images);
        return resultMap;
    }
}
