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
        String endpoint = environment.getProperty("simulation.minio.endpoint");
        String accessKey = environment.getProperty("simulation.minio.accessKey");
        String secretKey= environment.getProperty("simulation.minio.secretKey");
        MinioClient.Builder builder = MinioClient.builder();
        builder.endpoint(endpoint);
        builder.credentials(accessKey, secretKey);
        MinioClient client = builder.build();
        return client;
    }
}
