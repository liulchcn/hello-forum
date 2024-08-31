package com.hello.model.studio.pojos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "video")  // 指定MongoDB中的集合名称为 "video"
public class StudioVideo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id  // 指定主键字段，自动生成唯一标识符
    private ObjectId id;

    private Long authordId;  // 作者的用户ID，表示视频的创建者

    private String videoCover;  // 视频封面图片的URL地址

    private String videoUrl;  // 视频文件的URL地址

    private Date createdTime;  // 视频的创建时间，以时间戳（毫秒）表示

    private String intro;  // 视频的文字描述

    private String title;//视频的标题

    private Integer seeType;  // 视频的可见性类型，1-公开，2-私密，3-部分可见，4-不给谁看

    private List<Long> seeList;  // 可见列表，存储允许观看视频的用户ID（当seeType为3时使用）

    private List<Long> notSeeList;  // 不可见列表，存储不允许观看视频的用户ID（当seeType为4时使用）

    private String longitude;  // 视频发布时的经度信息，用于定位

    private String latitude;  // 视频发布时的纬度信息，用于定位

    private String locationName;  // 发布视频时的位置信息名称，例如地标或城市名
}
