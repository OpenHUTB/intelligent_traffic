package com.ruoyi.simulation.websocket;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
<<<<<<< HEAD
import com.ruoyi.simulation.call.CallMatlab;
import com.ruoyi.simulation.call.CallMinio;
import com.ruoyi.simulation.call.CallPython;
=======
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.simulation.call.CallBigModel;
import com.ruoyi.simulation.call.CallMinio;
import com.ruoyi.simulation.call.CallPaddleSpeech;
>>>>>>> 113f55efcf71008b44724489740af2ccb3687997
import com.ruoyi.simulation.call.CallVideo;
import com.ruoyi.simulation.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
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
<<<<<<< HEAD
    private static CallPython callPython;
    private static CallMatlab callMatlab;
=======
    private static CallPaddleSpeech callPython;
    private static CallBigModel callMatlab;
>>>>>>> 113f55efcf71008b44724489740af2ccb3687997
    private static CallVideo callVideo;
    private static CallMinio callMinio;
    private static Environment environment;

    @Autowired
    public void setFileUtil(FileUtil fileUtil) {
        WebSocketServer.fileUtil = fileUtil;
    }

    @Autowired
    public void setCallPython(CallPaddleSpeech callPython) {
        WebSocketServer.callPython = callPython;
    }

    @Autowired
    public void setCallMatlab(CallBigModel callMatlab) {
        WebSocketServer.callMatlab = callMatlab;
    }

    @Autowired
    public void setEnvironment(Environment environment) {
        WebSocketServer.environment = environment;
    }

    public static void setCallVideo(CallVideo callVideo) {
        WebSocketServer.callVideo = callVideo;
    }

    public static void setCallMinio(CallMinio callMinio) {
        WebSocketServer.callMinio = callMinio;
    }

    /**
     * 打开连接的回调函数
     *
     * @param session
     */
    @OnOpen
    public void openConnection(Session session) {
        this.session = session;
        webSocketMap.put(session.getId(), this);
        logger.info("----------------------------连接已建立-----------------------------");
    }

    /**
     * 关闭连接的回调函数
     *
     * @param session
     */
    @OnClose
    public void closeConnection(Session session) {
        webSocketMap.remove(session);
        logger.info("----------------------------连接已关闭-----------------------------");
    }

    /**
     * 收到客户端消息时的回调函数
     *
     * @param blob    语音命令
     * @param session
     */
    @OnMessage
    public void onMessage(byte[] blob, Session session) {
        logger.info("----------------------------收到消息-----------------------------");
<<<<<<< HEAD
        AjaxResult result = fileUtil.storeFileToDisk(blob);
        if (!result.get(CODE_TAG).equals(HttpStatus.SUCCESS)) {
            sendErrorResponse(String.valueOf(result.get(MSG_TAG)), session.getId());
            return;
        }
        String targetPath = String.valueOf(result.get(DATA_TAG));
        //第一步，将语音转换为文字
        result = callPython.generateText(targetPath);
        if (!result.get(CODE_TAG).equals(HttpStatus.SUCCESS)) {
            sendErrorResponse(String.valueOf(result.get(MSG_TAG)), session.getId());
            return;
        }
        String text = String.valueOf(result.get(DATA_TAG));
        logger.info("解析语音为文本如下：" + text);
        sendSuccessResponse(String.valueOf(result.get(MSG_TAG)),"语音识别成功!当前语音转换为如下文本：" + text, session.getId());

        //第二步，发送文字命令，调用“大模型”获取生成交通场景模型所需的代码
        result = callPython.generateCode(text);
        if (!result.get(CODE_TAG).equals(HttpStatus.SUCCESS)) {
            sendErrorResponse(String.valueOf(result.get(MSG_TAG)), session.getId());
            return;
        }
        List<String> codeList = (List<String>) result.get(DATA_TAG);
        String codeStr = ListUtil.toString(codeList);
        logger.info("代码如下：\n" + codeStr);
        sendSuccessResponse(String.valueOf(result.get(MSG_TAG)),"代码生成成功!根据文本命令生成的代码内容为：\n" + codeStr, session.getId());
        //第三步，调用“WebGL”渲染三维效果像素流
        result = callMatlab.generatePixelStream(codeList);
        if (!result.get(CODE_TAG).equals(HttpStatus.SUCCESS)) {
            sendErrorResponse(String.valueOf(result.get(MSG_TAG)), session.getId());
            return;
        }
        byte[] byteArray = (byte[]) result.get(DATA_TAG);
        String message = String.valueOf(result.get(MSG_TAG));
        StreamSet stream = getVoiceTips(message);
        stream.setScreen(byteArray);
        result = AjaxResult.success("三维场景像素流生成成功!", stream);
        sendMessage(JSON.toJSONString(result), session.getId());
    }
    /**
     * 发送消息到客户端
     *
     * @param message
     * @param sessionId
     */
    public void sendMessage(String message, String sessionId) {
        try {
            WebSocketServer server = webSocketMap.get(sessionId);
            server.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
=======
        try{
            String voicePath = fileUtil.storeFileToDisk(blob);
            logger.info("语音文件存储路径："+voicePath);
            //第一步，将语音转换为文字
            String text = callPython.generateText(voicePath);
            logger.info("语音识别后的文本为："+text);
            sendSuccessResponse("语音识别成功!当前语音转换为如下文本：" + text,"语音识别成功!当前语音转换为如下文本：" + text, session.getId());
            //第二步，发送文字命令，调用“大模型”获取生成交通场景模型所需的代码
            List<String> codeList = callMatlab.generateCode(text);
            String codeStr = ListUtil.toString(codeList);
            logger.info("调用大模型生成Matlab代码如下：\n" + codeStr);
            sendSuccessResponse("代码生成成功!","代码生成成功!根据文本命令生成的代码内容为：\n" + codeStr, session.getId());
            //第三步，调用“WebGL”渲染三维效果像素流
            byte[] byteArray = callMatlab.generatePixelStream(codeList);
            this.sendPixStreamResponse("三维场景像素流生成成功!",byteArray,session.getId());
        } catch (Exception e){
            sendErrorResponse(e.getMessage(), session.getId());
>>>>>>> 113f55efcf71008b44724489740af2ccb3687997
        }
    }

    /**
     * 返回执行失败响应
     * @param message 语音消息文本
     * @param sessionId socket会话id
     */
    public void sendErrorResponse(String message, String sessionId) {
<<<<<<< HEAD
        StreamSet stream = getVideoTips(message);
        AjaxResult result = AjaxResult.error(message, stream);
        sendMessage(JSON.toJSONString(result), sessionId);
=======
        try {
            StreamSet stream = getVideoTips(message);
            AjaxResult result = AjaxResult.error(message, stream);
            WebSocketServer server = webSocketMap.get(sessionId);
            server.session.getBasicRemote().sendText(JSON.toJSONString(result));
        } catch (Exception e) {
            logger.info(LoggerUtil.getLoggerStace(e));
        }
>>>>>>> 113f55efcf71008b44724489740af2ccb3687997
    }

    /**
     * 返回执行成功响应
     * @param message 语音消息文本
     * @param tips 文本提示消息
     * @param sessionId socket会话id
     */
    public void sendSuccessResponse(String message, String tips, String sessionId){
<<<<<<< HEAD
        StreamSet stream = getVideoTips(message);
        AjaxResult result = AjaxResult.success(tips, stream);
        sendMessage(JSON.toJSONString(result), sessionId);
=======
        try {
            StreamSet stream = getVideoTips(message);
            AjaxResult result = AjaxResult.success(tips, stream);
            WebSocketServer server = webSocketMap.get(sessionId);
            server.session.getBasicRemote().sendText(JSON.toJSONString(result));
        } catch (Exception e) {
            logger.info(LoggerUtil.getLoggerStace(e));
        }
    }

    /**
     * 返回像素流响应
     * @param message
     * @param byteArray
     * @param sessionId
     */
    public void sendPixStreamResponse(String message, byte[] byteArray, String sessionId){
        try {
            StreamSet stream = getVideoTips(message);
            stream.setScreen(byteArray);
            AjaxResult result = AjaxResult.success(message, stream);
            WebSocketServer server = webSocketMap.get(sessionId);
            server.session.getBasicRemote().sendText(JSON.toJSONString(result));
        } catch (Exception e) {
            logger.info(LoggerUtil.getLoggerStace(e));
        }
>>>>>>> 113f55efcf71008b44724489740af2ccb3687997
    }
    /**
     * 发送声音提示消息
     *
     * @param message
     */
    public StreamSet getVoiceTips(String message) {
        StreamSet stream = new StreamSet();
        String fileName = callPython.generateVoice(message);
        stream.setSound(fileName);
        return stream;
    }

    /**
     * 生成视频人脸提示
     * @param message
     * @return
     */
    public StreamSet getVideoTips(String message){
        StreamSet stream = new StreamSet();
        //根据文本生成对应的视频人脸，并返回该视频对应的fid值
        String fid = WebSocketServer.callVideo.generateVideo(message);
        //根据fid从minio中下载对应的视频
        String fileName = WebSocketServer.callMinio.download(fid);
        stream.setGraph(fid);
        return stream;
    }

    /**
     * 生成视频人脸提示
     * @param message
     * @return
     */
    public StreamSet getVideoTips(String message){
        StreamSet stream = new StreamSet();
        //根据文本生成对应的视频人脸，并返回该视频对应的fid值
        String fid = WebSocketServer.callVideo.generateVideo(message);
        //根据fid从minio中下载对应的视频
        String fileName = WebSocketServer.callMinio.download(message);
        stream.setGraph(fid);
        return stream;
    }
}
