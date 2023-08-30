package com.ruoyi.simulation.config;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

/**
 * minio配置类
 */
@Configuration
public class MinioConfig {
    @Resource
    private Environment environment;
    @Bean
    public MinioClient minioClient(){
        MinioClient.Builder builder = MinioClient.builder();
        builder.endpoint(environment.getProperty("simulation.minio.endpoint"));
        builder.credentials(environment.getProperty("simulation.minio.accessKey"),environment.getProperty("simulation.minio.secretKey"));
        MinioClient client = builder.build();
        return client;
    }
}
