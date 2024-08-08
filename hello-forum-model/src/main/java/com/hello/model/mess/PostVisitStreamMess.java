package com.hello.model.mess;

import lombok.Data;

@Data
public class PostVisitStreamMess {
    /**
     * 文章id
     */
    private Long PostId;
    /**
     * 阅读
     */
    private int view;
    /**
     * 收藏
     */
    private int collect;
    /**
     * 评论
     */
    private int comment;
    /**
     * 点赞
     */
    private int like;
}