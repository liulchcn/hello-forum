package com.hello.service.user.interceptor;

import com.hello.common.context.UserThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Slf4j
public class UserTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("预处理。request中的信息为：{}",request);
        //得到header中的信息
        String userId = request.getHeader("userId");
        //optioanl是用来解决空指针异常的问题，如果userId不为null，
        // 那么返回一个包含userId值的Optional对象；如果userId为null，
        // 那么返回一个空的Optional对象。
        Optional<String> optional = Optional.ofNullable(userId);
        log.info("得到的用户id为：{}",userId);
        if(optional.isPresent()){
            //把用户id存入threadloacl中
           /* WmUser wmUser = new WmUser();
            wmUser.setId(Integer.valueOf(userId));
            WmThreadLocalUtil.setUser(wmUser);*/
            UserThreadLocalUtil.setCurrentId(Integer.valueOf(userId));

            log.info("wmTokenFilter设置用户信息到threadlocal中...");
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("清理threadlocal...");
        UserThreadLocalUtil.removeCurrentId();
    }
}