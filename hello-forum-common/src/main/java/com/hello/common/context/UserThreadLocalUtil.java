package com.hello.common.context;

public class UserThreadLocalUtil {

    public static ThreadLocal<Integer> USER_THREAD_LOCAL = new ThreadLocal<>();

    public static void setCurrentId(Integer id) {
        USER_THREAD_LOCAL.set(id);
    }

    public static Integer getCurrentId() {
        return USER_THREAD_LOCAL.get();
    }

    public static void removeCurrentId() {
        USER_THREAD_LOCAL.remove();
    }

}
