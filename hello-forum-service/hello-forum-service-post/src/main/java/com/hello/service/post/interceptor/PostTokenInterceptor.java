package com.hello.service.post.interceptor;


import com.hello.common.context.UserThreadLocalUtil;
import com.hello.model.user.pojos.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;


@Slf4j
public class PostTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader("userId");
        log.info("存放UserID：{}",userId);
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
