package com.hello.model.search.vos;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class SearchPostAsInfoVO {

    private Long PostId;

    private String title;

    private Date publishTime;

    private String images;

    private Long authorId;

    private String authorName;

    private String content;
}
