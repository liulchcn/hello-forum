package com.hello.model.post.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class PostLoadDTO {

    Date maxHotTime;

    Date minHotTime;

    Integer size;

    String channelId;
}
