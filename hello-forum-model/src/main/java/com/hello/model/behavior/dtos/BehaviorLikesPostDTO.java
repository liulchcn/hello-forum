package com.hello.model.behavior.dtos;

import lombok.Data;

@Data
public class BehaviorLikesPostDTO {
    Long postId;
    /**
     * 喜欢内容类型
     * 0帖子
     * 1评论
     */
    short type;

    /**
     * 0喜欢
     * 1取消喜欢
     */
    short Operation;
}
