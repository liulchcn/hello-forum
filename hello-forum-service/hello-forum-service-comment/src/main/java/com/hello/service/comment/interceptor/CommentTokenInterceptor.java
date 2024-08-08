package com.hello.service.comment.interceptor;


import com.hello.common.context.UserThreadLocalUtil;
import com.hello.model.user.pojos.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;


public class CommentTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader("userId");
        if(userId != null){
            //存入到当前线程中
            User user = new User();
            user.setId(Integer.valueOf(userId));
            UserThreadLocalUtil.setCurrentId(user.getId());

        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocalUtil.removeCurrentId();
    }
}
