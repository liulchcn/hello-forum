package com.hello.service.studio;

import com.baomidou.mybatisplus.autoconfigure.DdlApplicationRunner;
//import com.heima.file.service.impl.MinIOFileStorageService;
import com.hello.fileStarter.config.MinIOConfig;
import com.hello.fileStarter.service.impl.MinIOFileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication(scanBasePackages = {"com.hello.fileStarter.service.impl", "com.hello.service.studio"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.hello.feign.api","com.hello.feign.api.post"})
@EnableAsync
public class ForumStudioApplication {
    public static void main(String[] args) {
//        ConcurrentHashMap
//        HashMap
        SpringApplication.run(ForumStudioApplication.class, args);
    }
    @Bean
    public DdlApplicationRunner ddlApplicationRunner(@Autowired(required = false) List ddlList) {
        return new DdlApplicationRunner(ddlList);
    }

}
