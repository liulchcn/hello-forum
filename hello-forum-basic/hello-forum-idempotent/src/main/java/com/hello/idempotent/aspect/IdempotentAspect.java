package com.hello.idempotent.aspect;

import com.hello.idempotent.annoation.Idempotent;
import com.hello.idempotent.handler.IdempotentContext;
import com.hello.idempotent.handler.IdempotentExecuteHandler;
import com.hello.idempotent.handler.IdempotentExecuteHandlerFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Aspect
public final class IdempotentAspect {

    /**
     * 增强方法标记 {@link Idempotent} 注解逻辑
     */
    @Around("@annotation(com.hello.idempotent.annoation.Idempotent)")
    public Object idempotentHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取到方法上的幂等注解实际数据
        Idempotent idempotent = getIdempotent(joinPoint);
        // 通过幂等场景以及幂等类型，获取幂等执行处理器
        IdempotentExecuteHandler instance = IdempotentExecuteHandlerFactory.getInstance(idempotent.scene(), idempotent.type());
        Object resultObj;
        try {
            // 执行幂等处理逻辑
            instance.execute(joinPoint, idempotent);
            // 如果幂等处理逻辑没有抛异常，处理中间业务
            resultObj = joinPoint.proceed();
            // 处理幂等后置逻辑，比如释放资源或者锁之类的
            instance.postProcessing();
        } catch (RuntimeException ex) {
            /**
             * 该异常为消息队列防重复提交独有，触发幂等逻辑时可能有两种情况：
             *    * 1. 消息还在处理，但是不确定是否执行成功，那么需要返回错误，方便 RocketMQ 再次通过重试队列投递
             *    * 2. 消息处理成功了，该消息直接返回成功即可
             */
//            if (!ex.getMessage() {
//                return null;
//            }
            throw ex;
        } catch (Throwable ex) {
            // 客户端消费存在异常，需要删除幂等标识方便下次 RocketMQ 再次通过重试队列投递
            instance.exceptionProcessing();
            throw ex;
        } finally {
            // 清理幂等容器上下文
            IdempotentContext.clean();
        }
        return resultObj;
    }

    public static Idempotent getIdempotent(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method targetMethod = joinPoint.getTarget().getClass().getDeclaredMethod(methodSignature.getName(), methodSignature.getMethod().getParameterTypes());
        return targetMethod.getAnnotation(Idempotent.class);
    }
}
