package com.hello.model.user.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
public class User implements Serializable {
    private static final Integer serialVersionUID = 1;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    @TableField("user_name")
    private String username;

    /**
     * 密码、通信等加密盐
     */
    @TableField("salt")
    private String salt;


    /**
     * 密码,md5加密
     */
    @TableField("password")
    private String password;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 昵称
     */
    @TableField("nick_name")
    private String nickName;
    /**
     * 头像
     */
    @TableField("avatar")
    private String avatar;


    /**
      0。锁定
     1正常用户
     2.大V
     */
    @TableField("status")
    private Short status;


    /**
     * 注册时间
     */
    @TableField("created_time")
    private Date createdTime;

}
