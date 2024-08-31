package com.hello.service.studio.handler;

// 处理器接口
public interface AuitHandler<T> {
    void setNext(AuitHandler<T> nextHandler);
    void handle(T context);
}
