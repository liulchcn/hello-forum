package com.hello.service.studio.service.impl;

import com.hello.common.result.Result;
import com.hello.common.utils.ProtostuffUtil;
import com.hello.feign.api.schedule.IScheduleClient;
import com.hello.model.enums.TaskTypeEnum;
import com.hello.model.schedule.dtos.Task;
import com.hello.model.studio.pojos.StudioPost;
import com.hello.service.studio.service.StudioPostTaskService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class StudioPostTaskServiceimpl implements StudioPostTaskService {
    @Autowired
    private IScheduleClient scheduleClient;
    @Override
//    @Async
    public Result addPostToTask(Integer id, Date publishTime) {
        log.info("添加任务：{} 到延迟队列------------start",id);
        StudioPost studioPost = new StudioPost();
        studioPost.setId(id);
        Task task = Task.builder()
                .taskType(TaskTypeEnum.POST_SCAN_TIME.getTaskType())
                .priority(TaskTypeEnum.POST_SCAN_TIME.getPriority())
                .executeTime(publishTime.getTime())
                .parameters(ProtostuffUtil.serialize(studioPost))
                .build();
        Result result = scheduleClient.addTask(task);
        log.info("添加任务：{} 到延迟队列------------end",task);
        return result;
    }
   @Scheduled(fixedRate = 1000)
    @Override
    public void scanPostsByTask() {
       /**
        * TODO 定时拉取发布
        * 目前想法是采用获得然后累加再发布还是直接最后一起审核
        * 又或者再建一个表用来获取post
        */
       Result result = scheduleClient.poll(TaskTypeEnum.POST_SCAN_TIME.getTaskType(),
               TaskTypeEnum.POST_SCAN_TIME.getPriority());
       if ((result.getCode().equals(200)||result.getCode().equals(201)&&result.getData() !=null)){
           //TODO

       }


   }





}
