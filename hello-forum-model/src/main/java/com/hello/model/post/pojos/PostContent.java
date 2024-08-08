package com.hello.model.post.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("post_content")
public class PostContent implements Serializable {
    @TableId(value = "id",type = IdType.ASSIGN_ID )
    private Long id;

    @TableField("post_id")
    private Long postId;

    private String content;

    @TableField("post_brief_introduction")
    private String postBriefIntroduction;
}
