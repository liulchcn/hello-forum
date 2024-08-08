package com.hello.service.studio.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hello.common.constants.DownOrUpConstants;
import com.hello.common.constants.StudioPostMessageConstants;
import com.hello.common.exception.DownOrUpException;
import com.hello.common.exception.SubmitPostsException;
import com.hello.common.result.Result;
import com.hello.model.studio.constants.StudioPostConstants;
import com.hello.model.studio.dtos.StudioPostDTO;
import com.hello.model.studio.pojos.StudioMaterial;
import com.hello.model.studio.pojos.StudioPost;
import com.hello.service.studio.mapper.StudioMaterialMapper;
import com.hello.service.studio.mapper.StudioPostMapper;
import com.hello.service.studio.service.StudioPostScanService;
import com.hello.service.studio.service.StudioPostService;
import com.hello.service.studio.service.StudioPostTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
@Transactional
public class StudioPostServiceImpl extends ServiceImpl<StudioPostMapper, StudioPost> implements StudioPostService {

    @Autowired
    private StudioPostMapper studioPostsMapper;
    @Autowired
    private StudioMaterialMapper studioMaterialMapper;
    @Autowired
    private StudioPostScanService studioPostScanService;
    @Autowired
    private StudioPostTaskService studioPostTaskService;
    @Autowired
    private KafkaTemplate kafkaTemplate;

    /**
     * 上传帖子
     * @param studioPostDTO
     */
    @Override
    public void submit(StudioPostDTO studioPostDTO) {
        if(studioPostDTO == null || studioPostDTO.getTitle()==null&&studioPostDTO.getContent()==null){
            throw new SubmitPostsException(StudioPostConstants.UPLOAD_POST_IS_NULL);
        }

        StudioPost studioPost = new StudioPost();
        BeanUtils.copyProperties(studioPostDTO,studioPost);
        if(studioPostDTO.getImages() != null && studioPostDTO.getImages().size() >0 ){
            String imagesStr = StringUtils.join(studioPostDTO.getImages(), StudioPostConstants.SEPARATOR);
            studioPost.setImages(imagesStr);
        }
        log.info("StudioPostServiceLmpl----保存修改前：{}",studioPost.getTitle());
        if(!save(studioPost)){
            throw new SubmitPostsException(StudioPostConstants.FAILED_UPLOAD_POST);
        }
        log.info("StudioPostServiceLmpl----保存修改后：{}",studioPost.getTitle());
        /**
         * 如果是草稿结束本次任务
         */
        if(studioPostDTO.getStatus().equals(StudioPost.Status.NORMAL.getCode())){
            return;
        }

        //不是草稿，保存帖子与素材关系
        List<String> materials = ectractUrlInfo(studioPost.getContent());
        saveRelativeInfoForContent(materials,studioPost.getId());
        saveRelativeInfoForCover(studioPostDTO,studioPost,materials);

        //审核帖子
        studioPostScanService.autoScanPosts(studioPost.getId());
//        log.info("帖子加入任务队列：{}",studioPost);
//        Result result = studioPostTaskService.addPostToTask(studioPost.getId(), studioPost.getPublishTime());
//        if(result.getCode() != 200 || result.getCode() != 201){
//            throw new SubmitPostsException(StudioPostConstants.POST_ADD_TO_TASK_FAILED);
//        }

    }

    @Override
    public void downOrUp(StudioPostDTO dto) {
        if (dto.getId()==null){
            throw new DownOrUpException(DownOrUpConstants.PARAM_INVALID);
        }
        StudioPost studioPost = getById(dto.getId());
        if(studioPost == null){
            throw new DownOrUpException(DownOrUpConstants.POST_DATA_NOT_EXIST);
        }
        if (studioPost.getStatus().equals(StudioPost.Status.PUBLISHED.getCode())){
            throw new DownOrUpException(DownOrUpConstants.POST_STATUS_IS_WRONG_CANT_DOWN_OR_UP);
        }
        if(studioPost.getEnable() != null && dto.getEnable()>-1&&dto.getEnable() <2 ){
            update(Wrappers.<StudioPost>lambdaUpdate().set(StudioPost::getEnable,dto.getEnable())
                    .eq(StudioPost::getId,studioPost.getId()));
        }
        if(studioPost.getPostId() != null){
            Map<String,Object> map = new HashMap<>();
            map.put(StudioPostMessageConstants.POST_ID,studioPost.getPostId());
            map.put(StudioPostMessageConstants.ENABLE,dto.getEnable());
            kafkaTemplate.send(StudioPostMessageConstants.POST_UP_OR_DOWN_TOPIC,JSON.toJSONString(map));
        }
    }

    private void saveRelativeInfoForCover(StudioPostDTO studioPostDTO, StudioPost studioPost, List<String> materials) {
        List<String> images = studioPostDTO.getImages();
        Integer studioPostId = studioPost.getId();
        if(images == null || images.size() == 0){
            throw new SubmitPostsException(StudioPostConstants.UPLOAD_POST_COVER_IS_NULL);
        }

        List<StudioMaterial> dbMaterials =
                studioMaterialMapper.selectList(Wrappers.<StudioMaterial>lambdaQuery().in(StudioMaterial::getUrl, images));
        log.info("找到的素材:{}",dbMaterials);
        if(dbMaterials == null  || dbMaterials.size()==0 || dbMaterials.size()!= materials.size()){
            throw new SubmitPostsException(StudioPostConstants.MATERIASL_REFERENCE_FAILED);
        }

        List<Integer> idList = dbMaterials.stream().map(StudioMaterial::getId).collect(Collectors.toList());

        studioMaterialMapper.saveRelations(idList,studioPostId,StudioPostConstants.STUDIO_COVER_REFERENCE);

    }

    private void saveRelativeInfoForContent(List<String> materials, Integer id) {
        if(materials ==null || materials.isEmpty()){return;}
        List<StudioMaterial> dbMaterials = studioMaterialMapper
                .selectList(Wrappers.<StudioMaterial>lambdaQuery().in(StudioMaterial::getUrl,materials));
        log.info("内容库素材：{}",materials);
        if(dbMaterials == null  || dbMaterials.size()==0 || dbMaterials.size()!= materials.size()){
            throw new SubmitPostsException(StudioPostConstants.MATERIASL_REFERENCE_FAILED);
        }

        List<Integer> idList = dbMaterials.stream().map(StudioMaterial::getId).collect(Collectors.toList());

        studioMaterialMapper.saveRelations(idList,id,StudioPostConstants.STUDIO_CONTENT_REFERENCE);

    }

    private List<String> ectractUrlInfo(String content) {
        List<String> materials = new ArrayList<>();
        List<Map> maps = JSON.parseArray(content, Map.class);
        log.info("内容中抽取出：{}",maps);

        for (Map map : maps) {
            log.info("内容中抽取出：{}",map.get(StudioPostConstants.FRONT_END_PASS_JSON_TYPE));
            if(map.get(StudioPostConstants.FRONT_END_PASS_JSON_TYPE).equals(StudioPostConstants.FRONT_END_PASS_JSON_IMAGE)){
                String imgUrl = (String) map.get(StudioPostConstants.FRONT_END_PASS_JSON__VALUE);
                materials.add(imgUrl);
            }
        }
        return materials;
    }
}
