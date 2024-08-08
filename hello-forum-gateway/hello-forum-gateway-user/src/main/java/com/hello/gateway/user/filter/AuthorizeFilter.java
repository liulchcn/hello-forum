package com.hello.gateway.user.filter;


import com.hello.gateway.user.util.UserJwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizeFilter implements Ordered, GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.获取request和response对象
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        //2.判断是否是登录
        if(request.getURI().getPath().contains("/login")){
            //放行
            log.info("登录请求放行");
            return chain.filter(exchange);
        }
        //2.判断是否是登录
        if(request.getURI().getPath().contains("/register")){
            //放行
            log.info("注册请求放行");
            return chain.filter(exchange);
        }



        //3.获取token
        String token = request.getHeaders().getFirst("token");

        // 4.判断token是否存在
        if(StringUtils.isBlank(token)){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        //5.判断token是否有效
        try {
            log.info("判断tocken是否有效");
            Claims claimsBody = UserJwtUtil.getClaimsBody(token);
            //是否是过期
            int result = UserJwtUtil.verifyToken(claimsBody);
            if(result == 1 || result  == 2){
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
            //获取用户信息
            Object userId = claimsBody.get("id");
            log.info("登录流程");
            //存储header中
            ServerHttpRequest serverHttpRequest = request.mutate().headers(httpHeaders -> {
                httpHeaders.add("userId", userId + "");
            }).build();
            //重置请求
            exchange.mutate().request(serverHttpRequest).build();
            log.info("token有效");
        }catch (Exception e){
            e.printStackTrace();
            log.info("token无效");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        log.info("exchange：{}",exchange);
//        log.info(exchange.toString());
        Mono<Void> filter = chain.filter(exchange);
        log.info("filter=={}",filter);
        //6.放行
        return filter;
    }

    /**
     * 优先级设置  值越小  优先级越高
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
