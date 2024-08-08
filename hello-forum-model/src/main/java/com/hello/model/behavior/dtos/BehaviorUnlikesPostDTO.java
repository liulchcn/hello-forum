package com.hello.model.behavior.dtos;

import lombok.Data;

@Data
public class BehaviorUnlikesPostDTO {
    Long postId;
    /**
     * 喜欢内容类型
     * 0帖子
     * 1评论
     */
    short type;

    /**
     * 0点踩
     * 1取消踩
     */
    short Operation;
}
