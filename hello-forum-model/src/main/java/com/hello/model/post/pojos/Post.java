package com.hello.model.post.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("post")
public class Post implements Serializable {

    @TableId(value = "id",type = IdType.ASSIGN_ID )
    private Long id;


    private String title;

    /**
     * 发帖人姓名
     */
    @TableField("blogger_name")
    private String bloggerName;

    @TableField("channel_id")
    private Integer channelId;

    @TableField("channel_name")
    private String channelName;

    private String images;

    private String labels;

    private Long likes;

    private Long collections;

    private Long comments;

    private Long views;

    private String province;

    private String city;

    private String county;

    @TableField("created_time")
    private Date createdTime;

    @TableField("publish_time")
    private Date publishTime;

    @TableField("updated_time")
    private Date updatedTime;
    /**
     * 帖子类型：
     * 1.引用帖子 2.原创帖子 3，热度引用帖子 4，热度原创帖子
     */
    private short flag;
}
