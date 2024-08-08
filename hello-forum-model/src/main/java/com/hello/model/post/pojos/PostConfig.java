package com.hello.model.post.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

@Data
@TableName("post_config")
public class PostConfig implements Serializable {
    public PostConfig(Long postId){
        this.postId = postId;
        this.commentStatus = (short)2;
        this.down = false;
        this.delete = false;
    }

    @TableId(value = "id",type = IdType.ASSIGN_ID )
    private Long id;

    @TableField("post_id")
    private Long postId;
    /**
     * 评论状态 0.不可评论 1.精选  2.可以随意评论
     */
    @TableField("comment_status")
    private Short commentStatus;

    /**
     * 是否下架
     */
    @TableField("is_down")
    private Boolean down;

    /**
     * 是否删除
     */
    @TableField("is_delete")
    private Boolean delete;

    @Alias("commentStatus")
    public enum Status{
        BAN((short)0),SELECT((short)1),NORMAL((short)2);
        short code;
        Status(short code){
            this.code = code;
        }
        public short getCode(){
            return this.code;
        }
    }
}
