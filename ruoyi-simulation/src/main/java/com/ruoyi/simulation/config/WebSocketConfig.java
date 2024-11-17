package com.ruoyi.simulation.config;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.simulation.util.LoggerUtil;
import com.ruoyi.simulation.websocket.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * websocket配置类
 */
@Configuration
public class WebSocketConfig {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    private WebSocketSession session;
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer(){
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(1024*1024);
        container.setMaxBinaryMessageBufferSize(1024*1024*50);
        return container;
    }
    @Bean
    public WebSocketClient webSocketClient(){
        return new StandardWebSocketClient();
    }
    public void connect(String url) throws ExecutionException, InterruptedException {
        this.session = new StandardWebSocketClient().doHandshake(new WebSocketHandler() {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                logger.info("客户端连接websocket成功!");
            }

            @Override
            public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
                logger.info("收到众天云消息!"+ JSON.toJSONString(message.getPayload()));
            }

            @Override
            public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {

            }

            @Override
            public boolean supportsPartialMessages() {
                return false;
            }
        },url).get();
    }

    /**
     * 发送消息
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.sendMessage(new TextMessage(message));
    }

    /**
     * 断开websocket连接
     */
    public void close(){
        try {
            this.session.close();
        } catch (IOException e) {
            LoggerUtil.printLoggerStace(e);
        }
    }
}
