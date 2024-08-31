package com.hello.idempotent.handler;

import com.hello.idempotent.enums.IdempotentSceneEnum;
import com.hello.idempotent.enums.IdempotentTypeEnum;

/**
 * 针对不同的幂等验证场景处理拆分一个个策略模式处理器
 */
public final class IdempotentExecuteHandlerFactory {

    /**
     * 获取幂等执行处理器
     *
     * @param scene 指定幂等验证场景类型
     * @param type  指定幂等处理类型
     * @return 幂等执行处理器
     */
    public static IdempotentExecuteHandler getInstance(IdempotentSceneEnum scene, IdempotentTypeEnum type) {
        IdempotentExecuteHandler result = null;
//        switch (scene) {
//            case RESTAPI -> {
//                switch (type) {
//                    case PARAM -> result = ApplicationContextHolder.getBean(IdempotentParamService.class);
//                    case TOKEN -> result = ApplicationContextHolder.getBean(IdempotentTokenService.class);
//                    case SPEL -> result = ApplicationContextHolder.getBean(IdempotentSpELByRestAPIExecuteHandler.class);
//                    default -> {
//                    }
//                }
//            }
//            case MQ -> result = ApplicationContextHolder.getBean(IdempotentSpELByMQExecuteHandler.class);
//            default -> {
//            }
        return null;
//        return result;
    }
}