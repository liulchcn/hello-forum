package com.hello.model.post.dtos;

import com.hello.model.post.pojos.Post;
import lombok.Data;

@Data
public class PostDTO extends Post{
    /**
     * 帖子内容
     */
    private String content;
    /**
     * 帖子简介
     */
    private String postBriefIntroduction;
}
