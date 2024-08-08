package com.hello.service.schedule.service;

import com.hello.model.schedule.dtos.Task;
import com.hello.model.schedule.vos.AddTaskVO;

public interface TaskService {
    /**
     * add Task
     * @param task
     * @return
     */
    public void add(Task task);

    public void cancelTask(Long taskId);
    public Task poll(int type,int priority);
}
