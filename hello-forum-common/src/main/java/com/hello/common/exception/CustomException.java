package com.hello.common.exception;


import com.hello.model.enums.AppHttpCodeEnum;

public class CustomException extends RuntimeException {

    private AppHttpCodeEnum appHttpCodeEnum;

    public CustomException(AppHttpCodeEnum appHttpCodeEnum){
        this.appHttpCodeEnum = appHttpCodeEnum;
    }

    public String getAppHttpCodeEnum() {
        return String.valueOf(appHttpCodeEnum);
    }
}
