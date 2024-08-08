package com.hello.model.mess;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdatePostMess {

    /**
     * 修改帖子的字段类型
      */
    private UpdatePostType type;

    private Long postId;
    /**
     * 修改数据的增量，可为正负
     */
    private Integer add;

    public enum UpdatePostType{
        COLLECTION,COMMENT,LIKES,VIEWS;
    }
}