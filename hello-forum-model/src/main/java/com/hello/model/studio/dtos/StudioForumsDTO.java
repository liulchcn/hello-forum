package com.hello.model.studio.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class StudioForumsDTO {

    /**
     * 标题
     */
    private String title;
    /**
     * 频道id
     */
    private Integer channel_id;

    /**
     * 标签
     */
    private String labels;

    /**
     * 提交时间
     */
    private Date created_time;
    /**
     *内容
     */
    private String content;

    /**
     * 上下架
     */
    private Short enable;
}
