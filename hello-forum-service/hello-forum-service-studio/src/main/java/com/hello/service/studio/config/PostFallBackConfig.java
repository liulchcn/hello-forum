package com.hello.service.studio.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.hello.feign.api.post.fallback")
public class PostFallBackConfig {
}
