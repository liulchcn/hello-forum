package com.hello.common.exception;

/**
 * 账号不存在异常
 */
public class UsernameAlreadyExistsException extends BaseException {

    public UsernameAlreadyExistsException() {
    }

    public UsernameAlreadyExistsException(String msg) {
        super(msg);
    }

}
