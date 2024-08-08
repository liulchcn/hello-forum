package com.hello.service.studio.idempotent.aspect;

import com.hello.common.result.Result;
import com.hello.service.studio.idempotent.annoation.RepeatSubmit;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestContextHolder;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class RepeatSubmitApect {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 定义一个线程池，不对主链路产生影响
     */
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
        1,1,1, TimeUnit.SECONDS,new LinkedBlockingQueue<>(100)
    );


    @Pointcut(value = "@annotation(repeatSubmit)")
    public void nonRepeatable(RepeatSubmit repeatSubmit) {
    }

    @Around(value = "nonRepeatable(repeatSubmit)",argNames = "joinPoint,repeatSubmit")
    public Object checking(ProceedingJoinPoint joinPoint, RepeatSubmit repeatSubmit) throws Throwable {
//        HttpServletRequest request = (HttpServletRequest) RequestContextHolder.getRequestAttributes();
//        threadPoolExecutor.execute(()->{
                    String type = repeatSubmit.limitType().name();
                    String key = ":repeat_submit:";
                    String requestURI = null;
                    String addr = null;
                    if (type.equalsIgnoreCase(RepeatSubmit.Type.PARAM.name())){
                        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
                        Method method = methodSignature.getMethod();
                        String className = method.getDeclaringClass().getName();
                        key = key + String.format("%s-%s-%s-%s",addr,requestURI,className,method);
                    }
                    key = DigestUtils.md5DigestAsHex(key.getBytes(StandardCharsets.UTF_8));
                    if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(key))){
                        stringRedisTemplate.opsForValue().setIfAbsent(key,"",repeatSubmit.interval(),repeatSubmit.timeUnit());
                        return joinPoint.proceed();

                    }else {
                        return Result.error(repeatSubmit.message());·
                    }

//        }
//        );
    }
}
