package com.hello.es.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class SearchPostVO {
    private Long id;
    private String title;
    private Date publishTime;
    private String images;
    private Long userId;
    private String content;

}
