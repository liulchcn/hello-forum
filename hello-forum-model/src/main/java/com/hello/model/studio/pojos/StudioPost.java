package com.hello.model.studio.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("studio_post")
public class StudioPost implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 自媒体用户ID
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 标题
     */
    @TableField("title")
    private String title;
    /**
     * 内容
     */
    @TableField("content")
    private String content;

    @TableField("post_brief_introduction")
    private String postBriefIntroduction;

    /**
     * 频道ID
     */
    @TableField("channel_id")
    private Integer channelId;
    /**
     * 标签
     */
    @TableField("labels")
    private String labels;
    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;
    /**
     * 提交时间
     */
    @TableField("submited_time")
    private Date submitedTime;
    /**
     * 状态
     */
    private Short status;
    /**
     *发布时间
     */
    @TableField("publish_time")
    private Date publishTime;
    /**
     * 原因
     */
    private String reason;
    /**
     * 发布的ID
     */
    @TableField("post_id")
    private Long postId;
    /**
     * 图片
     */
    private String images;
    /**
     * 是否禁用
     */
    private Short enable;
    @Alias("StudioPostStatus")
    public enum Status{
        NORMAL((short)0),SUBMIT((short)1),FAIL((short)2),ADMIN_AUTH((short)3),ADMIN_SUCCESS((short)4),SUCCESS((short)8),PUBLISHED((short)9);
        short code;
        Status(short code){
            this.code = code;
        }
        public short getCode(){
            return this.code;
        }
    }

}