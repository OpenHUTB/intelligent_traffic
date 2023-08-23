package com.ruoyi.simulation.websocket;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.simulation.util.*;
import jdk.internal.util.xml.impl.Input;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.ruoyi.common.core.domain.AjaxResult.*;

@Component
@ServerEndpoint("/simulation/websocket/")
public class WebSocketServer {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    private static Map<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<String, WebSocketServer>();
    private Session session = null;
    private static FileUtil fileUtil;
    private static CallPython callPython;
    private static CallMatlab callMatlab;

    @Autowired
    public void setFileUtil(FileUtil fileUtil) {
        WebSocketServer.fileUtil = fileUtil;
    }
    @Autowired
    public void setCallPython(CallPython callPython) {
        WebSocketServer.callPython = callPython;
    }
    @Autowired
    public void setCallMatlab(CallMatlab callMatlab) {
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
        if(!result.get(CODE_TAG).equals(HttpStatus.SUCCESS)){
            sendResponse(JSON.toJSONString(result),session.getId());
            return;
        }
        String targetPath = String.valueOf(result.get(DATA_TAG));
        //第一步，将语音转换为文字
        result = callPython.generateText(targetPath);
        if(!result.get(CODE_TAG).equals(HttpStatus.SUCCESS)){
            sendResponse(JSON.toJSONString(result),session.getId());
            return;
        }
        StreamSet stream = getVoiceTips("语音识别成功!","当前语音转换为如下文本："+result.get(DATA_TAG));
        sendResponse(JSON.toJSONString(AjaxResult.success(stream)), session.getId());
        //第二步，发送文字命令，调用“大模型”获的交通场景三维模型，如"创建一个驾驶场景对象scenario，并在场景中创建了一个车辆对象v1";
        String text = String.valueOf(result.get(DATA_TAG));
        logger.info("解析语音为文本如下："+text);
        result = callPython.generateCode(text);
        if(!result.get(CODE_TAG).equals(HttpStatus.SUCCESS)){
            sendResponse(JSON.toJSONString(result),session.getId());
            return;
        }
        stream = getVoiceTips("代码脚本生成成功!","根据文本命令生成的脚本内容为："+result.get(DATA_TAG));
        sendResponse(JSON.toJSONString(AjaxResult.success(stream)), session.getId());
        //第三步，调用“WebGL”渲染三维效果像素流
        List<String> codeList = (List<String>)result.get(DATA_TAG);
        logger.info("代码脚本如下："+codeList);
        result = callMatlab.generatePixelStream(codeList);
        if(!result.get(CODE_TAG).equals(HttpStatus.SUCCESS)){
            sendResponse(JSON.toJSONString(result),session.getId());
            return;
        }
        stream = getVoiceTips("像素流生成成功!",null);
        byte[] byteArray = (byte[])result.get(DATA_TAG);
        stream.setGraph(byteArray);
        sendResponse(JSON.toJSONString(AjaxResult.success(stream)),session.getId());
    }
    /**
     * 发送消息到客户端
     * @param message
     * @param sessionId
     */
    public void sendResponse(String message, String sessionId){
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
     * @param message
     */
    public StreamSet getVoiceTips(String message, String text){
        StreamSet stream = new StreamSet();
        stream.setMessage(message);
        if(!StringUtils.isEmpty(text)){
            stream.setMessage(stream.getMessage()+text);
        }
        AjaxResult result = callPython.generateVoice(message);
        if(result.get(CODE_TAG).equals(HttpStatus.SUCCESS)){
            String sound = (String)result.get(DATA_TAG);
            stream.setSound(sound);
        }
        return stream;
    }
}
