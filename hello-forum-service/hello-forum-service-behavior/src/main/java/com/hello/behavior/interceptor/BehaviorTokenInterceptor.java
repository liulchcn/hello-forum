package com.hello.behavior.interceptor;


import com.hello.common.context.UserThreadLocalUtil;
import com.hello.model.user.pojos.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class BehaviorTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader("userId");
        if(userId != null){
            log.info("将当前userId:{},存入线程",userId);
            //存入到当前线程中
            User user = new User();
            user.setId(Integer.valueOf(userId));
            UserThreadLocalUtil.setCurrentId(user.getId());
        }
        log.info("将当前userId:{},存入线程",userId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocalUtil.removeCurrentId();
    }
}
