package com.hello.model.studio.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class StudioUser implements Serializable {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("user_id")
    private Integer userId;

    @TableField("user_name")
    private String userName;

    @TableField("nickname")
    private String nickname;

    @TableField("image")
    private String image;

    @TableField("location")
    private String location;

    @TableField("phone")
    private String phone;

    @TableField("status")
    private Short status;

    @TableField("score")
    private Short score;

    @TableField("login_time")
    private Date loginTime;

    @TableField("created_time")
    private Date createdTime;

}
