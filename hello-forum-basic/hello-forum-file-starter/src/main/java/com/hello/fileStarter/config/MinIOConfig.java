package com.hello.fileStarter.config;

import com.hello.fileStarter.service.FileStorageService;
import com.hello.fileStarter.service.impl.MinIOFileStorageService;
import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Data
@Configuration
@EnableConfigurationProperties({MinIOConfigProperties.class})
@Slf4j
@ConditionalOnClass(FileStorageService.class)
public class MinIOConfig {

    @Autowired
    private MinIOConfigProperties minIOConfigProperties;

    @Bean
    public MinioClient buildMinioClient() {
        return MinioClient
                .builder()
                .credentials(minIOConfigProperties.getAccessKey(), minIOConfigProperties.getSecretKey())
                .endpoint(minIOConfigProperties.getEndpoint())
                .build();
    }
    @Bean
    public FileStorageService fileStorageService() {
        return new MinIOFileStorageService();
    }
    @PostConstruct
    public void init(){
        log.info("啊偶八八八八八八八八八八");
    }

}
