package com.hello.service.schedule.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hello.common.redis.CacheService;
import com.hello.model.schedule.contants.ScheduleConstants;
import com.hello.model.schedule.dtos.Task;
import com.hello.model.schedule.pojos.Taskinfo;
import com.hello.model.schedule.pojos.TaskinfoLogs;
import com.hello.model.schedule.vos.AddTaskVO;
import com.hello.service.schedule.mapper.TaskinfoLogsMapper;
import com.hello.service.schedule.mapper.TaskinfoMapper;
import com.hello.service.schedule.service.TaskService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.SchedulingException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.hello.model.schedule.contants.ScheduleConstants.FUTURE_TASK_SYNC;
import static com.hello.model.schedule.contants.ScheduleConstants.TASK_IN_REDIS_SEPARATOR;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private TaskinfoMapper taskinfoMapper;
    private CacheService cacheService;
    private TaskinfoLogsMapper taskinfoLogsMapper;
    private final RedissonClient redissonClient;


    @Override
    public void add(Task task) {
        log.info("将任务task加入队列：task:{}",task);
        AddTaskVO addTaskVO = new AddTaskVO();
        boolean success = addTaskToDB(task);
        log.info("将任务保存在DataBase:task--{}",task);
        if(success){
            addTaskToCache(task);
        }
    }

    @Override
    public void cancelTask(Long taskId) {
        Task task = updateDB(taskId, ScheduleConstants.CANCELLED);
        if (task!=null){
            removeTaskFromCache(task);
        }
    }

    @Override
    public Task poll(int type, int priority) {
        Task task = null;
        try{
            String key = type + TASK_IN_REDIS_SEPARATOR +priority;
            String task_json = stringRedisTemplate.opsForList().rightPop(ScheduleConstants.TOPIC + key);
            if(StringUtils.isNotBlank(task_json)){
                task  = JSON.parseObject(task_json,Task.class);
                updateDB(task.getTaskId(), ScheduleConstants.EXECUTED);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.info("poll Task Exception ");
        }
        return task;
    }

    /**
     * delete task from redis
     * @param task
     */
    private void removeTaskFromCache(Task task) {
        String key = task.getTaskType()+TASK_IN_REDIS_SEPARATOR+ task.getPriority();
        if(task.getExecuteTime()<=System.currentTimeMillis()){
            stringRedisTemplate.opsForList().remove(ScheduleConstants.TOPIC+key,0,JSON.toJSONString(task));
        }else{
            stringRedisTemplate.opsForZSet().remove(ScheduleConstants.FUTURE+key,0,JSON.toJSONString(task));
        }
    }

    private Task updateDB(Long taskId, int cancelled) {
        Task task = null;
        try{
            taskinfoMapper.deleteById(taskId);
            TaskinfoLogs taskinfoLogs = taskinfoLogsMapper.selectById(taskId);
            taskinfoLogs.setStatus(cancelled);
            taskinfoLogsMapper.updateById(taskinfoLogs);
            task = Task.builder().build();
            BeanUtils.copyProperties(taskinfoLogs,task);
            task.setExecuteTime(taskinfoLogs.getExecuteTime().getTime());
        }catch (Exception e){
            e.printStackTrace();
            log.info("task cancel exception taskId= {}",taskId);
        }
        return task;
    }

    private void addTaskToCache(Task task) {
        String key = task.getTaskType()+TASK_IN_REDIS_SEPARATOR+ task.getPriority();
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE,5);
        long nextExcuteTime = instance.getTimeInMillis();
        if(task.getExecuteTime() <= System.currentTimeMillis()){
            stringRedisTemplate.opsForList().leftPush(ScheduleConstants.TOPIC+key, JSON.toJSONString(task));
        }else if (task.getExecuteTime()<=nextExcuteTime){
            stringRedisTemplate.opsForZSet().add(ScheduleConstants.FUTURE+key,JSON.toJSONString(task), task.getExecuteTime());
        }
    }

    private boolean addTaskToDB(Task task) {
        Boolean flag = false;
        try{
            Taskinfo taskinfo = new Taskinfo();
            BeanUtils.copyProperties(task,taskinfo);
            taskinfo.setExecuteTime(new Date(task.getExecuteTime()));
            taskinfoMapper.insert(taskinfo);

            task.setTaskId(task.getTaskId());

            TaskinfoLogs taskinfoLogs = new TaskinfoLogs();
            BeanUtils.copyProperties(task,taskinfoLogs);
            taskinfoLogs.setVersion(1);
            taskinfoLogs.setStatus(ScheduleConstants.SCHEDULED);
            taskinfoLogsMapper.insert(taskinfoLogs);
            flag=true;


        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 定时刷新从zset中迁移到list中。
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void refresh(){
        log.info("执行定时任务。。。");
        RLock lock = redissonClient.getLock(FUTURE_TASK_SYNC);
        boolean tryLock = lock.tryLock();
        if (!tryLock){
            //TODO 给这个抛出异常升级一下
            throw new RuntimeException();
        }
        try {
            try {
                Set<String> funtureKeys = cacheService.scan(ScheduleConstants.FUTURE + TASK_IN_REDIS_SEPARATOR + "*");
                for (String funtureKey : funtureKeys) {
                    String topicKey = ScheduleConstants.TOPIC+funtureKey.split(ScheduleConstants.FUTURE)[1];
                    Set<String> tasks = stringRedisTemplate.opsForZSet().range(funtureKey, 0, System.currentTimeMillis());
                    if(!tasks.isEmpty()){
                        cacheService.refreshWithPipeline(funtureKey,topicKey,tasks);
                        log.info("成功将----：{}刷新到--：{}",funtureKey,topicKey);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                log.info("定时刷新从zset中迁移到list中出现异常");
            }
        }finally {
            lock.unlock();
        }
    }






    @Scheduled(cron = "0 */5 * * * ?")
    @PostConstruct  //项目启动时，同步数据
    public void reloadData(){
        log.info("查询符合条件的数据从Mysql同步到redis");
        //清理的操作 清理缓存的数据
        clearCache();

        //查询符合条件的数据进行同步
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);

        List<Taskinfo> taskinfoList = taskinfoMapper.selectList(Wrappers.<Taskinfo>lambdaQuery().lt(Taskinfo::getExecuteTime, calendar.getTime()));
        if(taskinfoList != null && taskinfoList.size() > 0){
            for (Taskinfo taskinfo : taskinfoList) {
                Task task = Task.builder()
                        .build();
                BeanUtils.copyProperties(taskinfo,task);
                task.setExecuteTime(taskinfo.getExecuteTime().getTime());
                addTaskToCache(task);
            }
        }

    }

    /**
     * 清理缓存中的数据
     */
    private void clearCache(){
        log.info("定时任务，清除缓存中的数据....");
        try{
            Set<String> topicKeys = cacheService.scan(ScheduleConstants.TOPIC + "*");
            Set<String> futureKeys = cacheService.scan(ScheduleConstants.FUTURE + "*");
            stringRedisTemplate.delete(topicKeys);
            stringRedisTemplate.delete(futureKeys);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
