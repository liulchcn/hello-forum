package com.hello.model.studio.dtos;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class UploadVideoDTO {

    private String videoCover;

    private String videoUrl;

    private String title;

    private String intro;  // 视频的文字描述

    private Integer seeType;  // 视频的可见性类型，1-公开，2-私密，3-部分可见，4-不给谁看

    private List<Long> seeList;  // 可见列表，存储允许观看视频的用户ID（当seeType为3时使用）

    private List<Long> notSeeList;  // 不可见列表，存储不允许观看视频的用户ID（当seeType为4时使用）

    private String longitude;  // 视频发布时的经度信息，用于定位

    private String latitude;  // 视频发布时的纬度信息，用于定位

    private String locationName;  // 发布视频时的位置信息名称，例如地标或城市名
}
