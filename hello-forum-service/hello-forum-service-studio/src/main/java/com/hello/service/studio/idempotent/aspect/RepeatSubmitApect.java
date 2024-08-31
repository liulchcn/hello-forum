package com.hello.service.studio.idempotent.aspect;

import cn.hutool.json.JSONUtil;
import com.hello.common.result.Result;
import com.hello.service.studio.idempotent.annoation.RepeatSubmit;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class RepeatSubmitApect {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Pointcut(value = "@annotation(repeatSubmit)")
    public void nonRepeatable(RepeatSubmit repeatSubmit) {
    }

    @Around(value = "nonRepeatable(repeatSubmit)", argNames = "joinPoint,repeatSubmit")
    //jointPoint中存放？
    public Object checking(ProceedingJoinPoint joinPoint, RepeatSubmit repeatSubmit) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        //请求参数拼接
        String nowParams = argsArrayToString(joinPoint.getArgs());

        String submitKey = String.valueOf(request.getHeaders("token"));
        log.info("submitKey:{}",submitKey);
        if(StringUtils.isBlank(submitKey)){
            submitKey = request.getRequestURI();
        }

        String type = repeatSubmit.limitType().name();
        String key = "repeat_submit:" + submitKey;

        String requestURI = request.getRequestURI();
        String addr = null;

        if (type.equalsIgnoreCase(RepeatSubmit.Type.PARAM.name())) {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            String className = method.getDeclaringClass().getName();
            key = key + String.format("-%s-%s", method,nowParams);
            log.info("key:{}",key);
        }
        key = DigestUtils.md5DigestAsHex(key.getBytes(StandardCharsets.UTF_8));
        if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(key))) {
            stringRedisTemplate.opsForValue().setIfAbsent(key, "", repeatSubmit.interval(), repeatSubmit.timeUnit());
            return joinPoint.proceed();
        } else {
            return Result.error(repeatSubmit.message());
        }
    }

    /**
     * 进行参数拼接
     * @param paramsArray
     * @return
     */
    private String argsArrayToString(Object[] paramsArray){
        StringBuilder params = new StringBuilder();
        if(paramsArray != null && params.length() > 0){
            for (Object o : paramsArray) {
                //在参数拼接过程中需要进行过滤
                if(Objects.nonNull(o)&&!isFilterObject(o)){
                    try{
                        params.append(JSONUtil.toJsonStr(o)).append("_");
                    }catch (Exception e){

                    }
                }
            }
        }
        return params.toString().trim();
    }

    public boolean isFilterObject(final Object o){
        Class<?> clazz=o.getClass();
        //如果是数组且为文件类型的，需要过滤
        if(clazz.isArray()){
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        //如果集合类型并且属于文件对象需要过滤
        }else if(Collection.class.isAssignableFrom(clazz)){
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof  HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }


}
