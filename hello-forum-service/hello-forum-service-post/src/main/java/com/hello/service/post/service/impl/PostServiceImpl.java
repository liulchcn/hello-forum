package com.hello.service.post.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hello.common.constants.HotPostConstants;
import com.hello.common.constants.SearchPostConstants;
import com.hello.common.exception.SubmitPostsException;
import com.hello.common.result.Result;
import com.hello.model.enums.AppHttpCodeEnum;
import com.hello.model.mess.PostVisitStreamMess;
import com.hello.model.post.dtos.PostDTO;
import com.hello.model.post.dtos.PostLoadDTO;
import com.hello.model.post.pojos.Post;
import com.hello.model.post.pojos.PostConfig;
import com.hello.model.post.pojos.PostContent;
import com.hello.model.post.vos.HotPostVO;
import com.hello.model.search.vos.SearchPostAsInfoVO;
import com.hello.service.post.constants.PostConstants;
import com.hello.service.post.mapper.PostConfigMapper;
import com.hello.service.post.mapper.PostContentMapper;
import com.hello.service.post.mapper.PostMapper;
import com.hello.service.post.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.SpringTemplateLoader;

import java.util.*;
import java.util.stream.Collectors;

import static com.hello.common.constants.HotPostConstants.*;

@Service
@Slf4j
@Transactional
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {
    @Autowired
    private PostConfigMapper postConfigMapper;
    @Autowired
    private PostContentMapper postContentMapper;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private KafkaTemplate<String ,String> kafkaTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result savePost(PostDTO postDTO) {

        if(postDTO==null){
            return Result.error(PostConstants.DATA_NOT_EXIST);
        }
        log.info("postDTO为：{}",postDTO.getTitle());
        Post post = new Post();
        BeanUtils.copyProperties(postDTO,post);
        if(postDTO.getId()==null){
            save(post);

            PostConfig postConfig = new PostConfig(post.getId());
            log.info("保存帖子配置：{}",postConfig);
            postConfigMapper.insert(postConfig);

            PostContent postContent = new PostContent();
            postContent.setPostId(post.getId());
            postContent.setContent(postDTO.getContent());
            postContent.setPostBriefIntroduction(postDTO.getPostBriefIntroduction());
            log.info("保存帖子内容：{}",postContent);
            postContentMapper.insert(postContent);
        }else {
            log.info("保存帖子：{}");
            updateById(post);
            PostContent postContent = postContentMapper.selectOne(Wrappers.<PostContent>lambdaQuery().eq(PostContent::getPostId, postDTO.getId()));
            if(postContent==null){
                return Result.error(PostConstants.UPDATE_POST_FAILED);
            }
            postContent.setContent(postDTO.getContent());
            postContent.setPostBriefIntroduction(postDTO.getPostBriefIntroduction());
            postContentMapper.updateById(postContent);
        }
        //TODO 创建索引
//        createPostESIndex(post,postDTO.getContent());

        return Result.success(post.getId());
    }

    @Override
    public void updateScore(PostVisitStreamMess postVisitStreamMess) {
        Post post = updatePost(postVisitStreamMess);
        long score = computeScore(post);

        replaceDataToRedis(post, score, HOT_POST_FIRST_PAGE_TOPIC+post.getChannelId());
    }

    @Override
    public List<HotPostVO> loadHomePage(PostLoadDTO postLoadDTO) {
        String key = HOT_POST_FIRST_PAGE_TOPIC;
        if(postLoadDTO.getChannelId()==null){
            key= HOT_POST_FIRST_PAGE_TOPIC + DEFAULT_TAG;
        }else {
            key= HOT_POST_FIRST_PAGE_TOPIC + postLoadDTO.getChannelId();
        }
        String jsonStr = stringRedisTemplate.opsForValue().get(key);
        if(jsonStr!=null){
            List<HotPostVO> hotPostVOS = JSON.parseArray(jsonStr, HotPostVO.class);
            return hotPostVOS;
        }
        return load(key,postLoadDTO);
    }

    private List<HotPostVO> load(String key, PostLoadDTO postLoadDTO) {
        Integer size = postLoadDTO.getSize();
        if(size == null || size == 0){
            size = 10;
        }
        size = Math.min(size,MAX_PAGE_SIZE);
        postLoadDTO.setSize(size);

        if(postLoadDTO.getMaxHotTime() == null) {postLoadDTO.setMaxHotTime(new Date());}
        if(postLoadDTO.getMinHotTime() == null) {
            // 获取当前时间
            Date now = new Date();

            // 使用 Calendar 来操作日期
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);

            // 将时间提前三天
            calendar.add(Calendar.DAY_OF_MONTH, -3);
            Date threeDaysEarlier = calendar.getTime();

            // 设置到 DTO
            postLoadDTO.setMinHotTime(threeDaysEarlier);
        }
        List<Post> load = postMapper.loadPostList(postLoadDTO);
        List<HotPostVO> hotPostVOS = new ArrayList<>();
        for (Post post : load) {
            HotPostVO hotPostVO = new  HotPostVO();
            BeanUtils.copyProperties(post,hotPostVO);
            hotPostVOS.add(hotPostVO);

        }
        return hotPostVOS;
    }

    private void replaceDataToRedis(Post post, long score, String s) {
        String postList = stringRedisTemplate.opsForValue().get(s);
        if(StringUtils.isNotBlank(postList)){
            List<HotPostVO> hotPostVOS = JSON.parseArray(postList, HotPostVO.class);

            boolean flag = true;

            for (HotPostVO hotPostVO : hotPostVOS) {
                if(hotPostVO.getId().equals(post.getId())){
                    hotPostVO.setScore((int) score);
                    flag = false;
                    break;
                }
            }

            if(flag){
                if(hotPostVOS.size() >= 30){
                    List<HotPostVO> hotPostVOList = hotPostVOS.stream()
                            .sorted(Comparator.comparing(HotPostVO::getScore).reversed())
                            .collect(Collectors.toList());

                    HotPostVO lastPostVO = hotPostVOList.get(hotPostVOList.size() - 1);

                    if(lastPostVO.getScore() < score){
                        hotPostVOList.remove(lastPostVO);
                        HotPostVO hotPostVO = new HotPostVO();
                        BeanUtils.copyProperties(post,hotPostVO);
                    }
                }
            }else {
                HotPostVO hotPostVO = new HotPostVO();
                BeanUtils.copyProperties(post,hotPostVO);
                hotPostVO.setScore((int) score);
                hotPostVOS.add(hotPostVO);
            }

            List<HotPostVO> hotPostVOList = hotPostVOS.stream().sorted(Comparator.comparing(HotPostVO::getScore).reversed()).collect(Collectors.toList());
            stringRedisTemplate.opsForValue().set(s,JSON.toJSONString(hotPostVOList));
        }
    }

    private long computeScore(Post post) {
        long score = 0;
        if(post.getLikes() != null){
            score += post.getLikes()* HOT_POST_LIKE_WEIGHT;
        }
        if(post.getCollections() != null){
            score += post.getCollections()*HOT_POST_COLLECTION_WEIGHT;
        }
        if(post.getComments() != null){
            score += post.getComments()*HOT_POST_COMMENT_WEIGHT;
        }
        if(post.getViews() != null){
            score += post.getViews()*HOT_POST_VIEW_WEIGHT;
        }

        return score;
    }

    private Post updatePost(PostVisitStreamMess postVisitStreamMess) {
        Post post = getById(postVisitStreamMess.getPostId());
        post.setComments(post.getComments() == null ? postVisitStreamMess.getComment() :
                (post.getComments() + postVisitStreamMess.getComment()));
        post.setCollections(post.getCollections()==null ? postVisitStreamMess.getCollect():
                post.getCollections()+postVisitStreamMess.getCollect());
        post.setLikes(post.getLikes()==null ? postVisitStreamMess.getLike() :
                post.getLikes()+postVisitStreamMess.getLike());
        post.setViews(post.getViews() == null ? postVisitStreamMess.getView():
                postVisitStreamMess.getView()+post.getViews());
        updateById(post);
        return post;
    }

    private void createPostESIndex(Post post, String content) {
        SearchPostAsInfoVO searchPostAsInfoVO = SearchPostAsInfoVO.builder()
                .PostId(Long.valueOf(post.getId()))
                .title(post.getTitle())
                .content(content)
                .images(post.getImages())
                .build();
        kafkaTemplate.send(SearchPostConstants.POST_ES_SYNC_TOPIC, JSON.toJSONString(searchPostAsInfoVO));


    }
}
