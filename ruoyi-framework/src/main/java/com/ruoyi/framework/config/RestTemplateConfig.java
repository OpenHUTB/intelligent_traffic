package com.ruoyi.framework.config;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
/**
 * @classname:  RestTemplateConfig
 * @author: ouyanghua
 * @description: 远程调用接口config
 * @date: 2023/10/22
 * @version: v1.0
 **/
@Configuration
public class RestTemplateConfig {



    @Bean
    public ClientHttpRequestFactory httpRequestFactory() {
        // 创建并配置HttpComponentsClientHttpRequestFactory
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

    @Bean
    public RestTemplate restTemplate() {
        // 创建并配置RestTemplate，使用上面定义的httpRequestFactory
        return new RestTemplate(httpRequestFactory());
    }

    @Bean
    // 创建并配置HttpClient，用于处理HTTP请求
    public HttpClient httpClient() {

        // 注册HTTP和HTTPS协议的连接工厂
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();

        // 创建连接管理器，管理连接池
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(3000);
        connectionManager.setDefaultMaxPerRoute(1000);

        RequestConfig requestConfig = RequestConfig.custom()
                //读取数据超时时间
                .setSocketTimeout(3000)

                // 建立连接超时时间
                .setConnectTimeout(3000)

                // 获取连接的超时时间
                .setConnectionRequestTimeout(3000)
                .build();

        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .build();
    }
}