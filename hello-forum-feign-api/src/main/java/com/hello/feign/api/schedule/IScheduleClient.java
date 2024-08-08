package com.hello.feign.api.schedule;

import com.hello.common.result.Result;
import com.hello.model.schedule.dtos.Task;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("forum-schedule")
public interface IScheduleClient {
    @PostMapping("/api/v1/task/add")
    public Result addTask(@RequestBody Task task);
    /**
     * 取消任务
     * @param taskId        任务id
     * @return              取消结果
     */
    @GetMapping("/api/v1/task/cancel/{taskId}")
    public Result cancelTask(@PathVariable("taskId") long taskId);

    /**
     * 按照类型和优先级来拉取任务
     * @param type
     * @param priority
     * @return
     */
    @GetMapping("/api/v1/task/poll/{type}/{priority}")
    public Result poll(@PathVariable("type") int type,@PathVariable("priority")  int priority);

}
