package com.hello.service.studio.service;

import com.hello.common.result.Result;

import java.util.Date;

public interface StudioPostTaskService {
    /***
     * 定时发布
     * @param id
     * @param publishTime
     * @return
     */
    public Result addPostToTask(Integer id, Date publishTime);

    /**
     * 消费延迟队列数据
     */
    public void scanPostsByTask();
}
