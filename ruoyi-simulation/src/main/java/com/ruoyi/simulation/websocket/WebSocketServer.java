package com.ruoyi.simulation.websocket;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.simulation.util.CallMatlab;
import com.ruoyi.simulation.util.CallPython;
import com.ruoyi.simulation.util.FileUtil;
import com.ruoyi.simulation.util.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.ruoyi.common.core.domain.AjaxResult.*;

@Component
@ServerEndpoint("/simulation/websocket/")
public class WebSocketServer {
    private Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    private static Map<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<String, WebSocketServer>();
    private Session session = null;
    private static FileUtil fileUtil;
    private static CallPython callPython;
    private static CallMatlab callMatlab;

    @Autowired
    public static void setFileUtil(FileUtil fileUtil) {
        WebSocketServer.fileUtil = fileUtil;
    }
    @Autowired
    public static void setCallPython(CallPython callPython) {
        WebSocketServer.callPython = callPython;
    }
    @Autowired
    public static void setCallMatlab(CallMatlab callMatlab) {
        WebSocketServer.callMatlab = callMatlab;
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
     * @param blob 语音命令
     * @param session
     */
    @OnMessage
    public void onMessage(byte[] blob, Session session){
        logger.info("----------------------------收到消息-----------------------------");
        AjaxResult result = fileUtil.storeFileToDisk(blob);
        if(result.get(CODE_TAG).equals(HttpStatus.SUCCESS)){
            String targetPath = String.valueOf(result.get(DATA_TAG));
            //第一步，将语音转换为文字
            result = callPython.generateText(targetPath);
            if(!result.get(CODE_TAG).equals(HttpStatus.SUCCESS)){
                sendVoiceTips("语音识别成功!",session.getId());
                //第二步，发送文字命令，调用“大模型”获的交通场景三维模型，如"创建一个驾驶场景对象scenario，并在场景中创建了一个车辆对象v1";
                String text = String.valueOf(result.get(DATA_TAG));
                result = callPython.generateCode(text);
                if(!result.get(CODE_TAG).equals(HttpStatus.SUCCESS)){
                    sendVoiceTips("代码脚本生成成功!",session.getId());
                    //第三步，调用“WebGL”渲染三维效果像素流
                    List<String> codeList = (List<String>)result.get(DATA_TAG);
                    result = callMatlab.generatePixelStream(codeList);
                    if(!result.get(CODE_TAG).equals(HttpStatus.SUCCESS)){
                        result.put(MSG_TAG,"像素流生成成功!");
                        send(JSON.toJSONString(result),session.getId());
                        return;
                    }
                }
            }
        }
        send(JSON.toJSONString(result),session.getId());
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

    /**
     * 发送声音提示消息
     * @param text "语音识别成功!"
     * @param sessionId
     */
    public void sendVoiceTips(String text, String sessionId){
        try {
            AjaxResult result = callPython.generateVoice(text);
            if (result.get(CODE_TAG).equals(HttpStatus.SUCCESS)) {
                result.put("MSG_TAG", text);
                send(JSON.toJSONString(result), session.getId());
            }
        } catch (Exception e) {
            logger.error(LoggerUtil.getLoggerStace(e));
        }
    }
}
