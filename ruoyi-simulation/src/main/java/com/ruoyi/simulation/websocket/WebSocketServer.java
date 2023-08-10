package com.ruoyi.simulation.websocket;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.simulation.service.IScenarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/simulation/websocket/")
public class WebSocketServer {
    private Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    private static Map<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<String, WebSocketServer>();
    private Session session = null;
    private static IScenarioService scenarioService;
    @Autowired
    public void setScenarioService(IScenarioService scenarioService){
        WebSocketServer.scenarioService = scenarioService;
    }
    /**
     * 打开连接的回调函数
     * @param session
     */
    @OnOpen
    public void openConnection(Session session){
        this.session = session;
        webSocketMap.put(session.getId(),this);
        logger.info("----------------------------连接已建立-----------------------------");
    }

    /**
     * 关闭连接的回调函数
     * @param session
     */
    @OnClose
    public void closeConnection(Session session){
        webSocketMap.remove(session);
        logger.info("----------------------------连接已关闭-----------------------------");
    }

    /**
     * 收到客户端消息时的回调函数
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session){
        logger.info("----------------------------收到消息-----------------------------");
        logger.info(message);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        AjaxResult result = this.scenarioService.getTrafficScenarios(message);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.send(JSON.toJSONString(result), session.getId());
    }

    /**
     * 发送消息到客户端
     * @param message
     * @param sessionId
     */
    public void send(String message, String sessionId){
        WebSocketServer server = webSocketMap.get(sessionId);
        Session session = server.session;
        synchronized (session){
            try {
                server.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
