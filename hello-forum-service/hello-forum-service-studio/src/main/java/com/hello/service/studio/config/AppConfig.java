package com.hello.service.studio.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.hello.fileStarter", "com.hello.service.studio"})
public class AppConfig {
}
