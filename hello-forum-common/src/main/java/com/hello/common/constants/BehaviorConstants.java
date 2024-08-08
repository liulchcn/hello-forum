package com.hello.common.constants;

public class BehaviorConstants {

    /**
     * 常用参数
     */
    //点赞
    public static final short OPERATION_LIKE = 0;
   //取消点赞
    public static final short OPERATION_CANCEL_LIKE = 1;

    //点踩
    public static final short OPERATION_UNLIKE =0;

    //取消点踩
    public static final short OPERATION_CANCEL_UNLIKE = 1;

    //给评论点赞
    public static final short LIKE_TYPE_COMMENT = 1;
    //给帖子点赞
    public static final short LIKE_TYPE_POST = 0;



    /**
     * 配合redis
     */
    public static final String LIKE_BEHAVIOR="LIKE-BEHAVIOR-";
    public static final String UN_LIKE_BEHAVIOR="UNLIKE-BEHAVIOR-";
    public static final String COLLECTION_BEHAVIOR="COLLECTION-BEHAVIOR-";
    public static final String READ_BEHAVIOR="READ-BEHAVIOR-";
    public static final String APUSER_FOLLOW_RELATION="APUSER-FOLLOW-";
    public static final String APUSER_FANS_RELATION="APUSER-FANS-";

    /**
     * 常见错误列下
     */
    public static final String PARAM_INVALID_EXCEPTION="参数错误";
    public static final String DUPILICAT_LIKE="重复点赞";
    public static final String DUPILICAT_UNLIKE="重复点踩";


}
