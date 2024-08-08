package com.hello.model.studio.constants;

public class StudioPostConstants {


    public static final String SEPARATOR = ",";
    public static final String STUDIO_POST_CONTENT = "content";
    public static final String STUDIO_POST_IMAGES = "images";

    //前端传递的数据格式
    public static final String FRONT_END_PASS_JSON_TYPE = "type";
    public static final String FRONT_END_PASS_JSON_IMAGE = "image";
    public static final String FRONT_END_PASS_JSON__TEXT = "text";
    public static final String FRONT_END_PASS_JSON__VALUE= "value";

    //报错异常
    public static final String UPLOAD_POST_IS_NULL = "上传的帖子为空!";
    public static final String UPLOAD_POST_COVER_IS_NULL = "上传的帖子的封面为空!";
    public static final String FAILED_UPLOAD_POST = "上传帖子失败！";
    public static final String MATERIASL_REFERENCE_FAILED = "素材引用失效！";
    public static final String POST_TO_BE_SCANNED_NOT_FOUND = "要被扫描的帖子找不到了！";
    public static final String POST_SAVED_FAILED = "保存帖子数据失败！";
    public static final String POST_ADD_TO_TASK_FAILED = "添加发表帖子任务失败！";

    //素材引用关系
    public static final Integer STUDIO_CONTENT_REFERENCE = 0 ;
    public static final Integer STUDIO_COVER_REFERENCE = 1 ;
    public static final String SCAN_POST_SUCCESS = "审核成功" ;
    public static final Short POST_SCANNED_SUCCESS_STATUS = 9 ;


}
