package com.hello.service.schedule.feign;

import com.hello.common.result.Result;
import com.hello.feign.api.schedule.IScheduleClient;
import com.hello.model.schedule.dtos.Task;
import com.hello.model.schedule.vos.AddTaskVO;
import com.hello.service.schedule.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScheduleClient implements IScheduleClient {
    @Autowired
    private TaskService taskService;

    @PostMapping("/api/v1/task/add")
    @Override
    public Result<AddTaskVO> addTask(Task task) {
        taskService.add(task);
        return Result.success();
    }

    @Override
    public Result cancelTask(long taskId) {
        taskService.cancelTask(taskId);
        return Result.success();
    }

    @Override
    public Result poll(int type, int priority) {
        Task task = taskService.poll(type, priority);
        return Result.success(task);
    }
}
