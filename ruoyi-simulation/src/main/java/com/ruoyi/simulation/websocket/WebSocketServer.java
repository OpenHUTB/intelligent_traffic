package com.ruoyi.simulation.websocket;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.simulation.call.CallBigModel;
import com.ruoyi.simulation.call.CallMinio;
import com.ruoyi.simulation.call.CallPaddleSpeech;
import com.ruoyi.simulation.call.CallVideo;
import com.ruoyi.simulation.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.annotation.Resource;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.ruoyi.common.core.domain.AjaxResult.CODE_TAG;

@Component
@ServerEndpoint("/simulation/websocket/")
@CrossOrigin
public class WebSocketServer {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    private static Map<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<String, WebSocketServer>();
    private static Map<String, List<String>> codeMap = new ConcurrentHashMap<String, List<String>>();
    private Session session = null;
    private static FileOperatorUtil fileUtil;
    private static CallPaddleSpeech callPaddleSpeech;
    private static CallBigModel callBigModel;
    private static CallVideo callVideo;
    private static CallMinio callMinio;
    private static RedisTemplate<String,Boolean> redisTemplate;
    @Autowired
    public void setFileUtil(FileOperatorUtil fileUtil) {
        WebSocketServer.fileUtil = fileUtil;
    }
    @Autowired
    public void setCallPaddleSpeech(CallPaddleSpeech callPaddleSpeech) {
        WebSocketServer.callPaddleSpeech = callPaddleSpeech;
    }
    @Autowired
    public void setCallBigModel(CallBigModel callBigModel) {
        WebSocketServer.callBigModel = callBigModel;
    }
    @Autowired
    public void setCallVideo(CallVideo callVideo) {
        WebSocketServer.callVideo = callVideo;
    }
    @Autowired
    public void setCallMinio(CallMinio callMinio) {
        WebSocketServer.callMinio = callMinio;
    }
    @Resource
    public void setRedisTemplate(RedisTemplate<String,Boolean> redisTemplate){
        WebSocketServer.redisTemplate = redisTemplate;
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
        webSocketMap.remove(session.getId());
        redisTemplate.delete(session.getId());
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
        try{
            //存储话筒接收到的语音文件，并返回语音文件存储位置的绝对路径
            String voicePath = fileUtil.storeFileToDisk(blob,"wav");
            logger.info("语音文件存储路径："+voicePath);
            //语音唤醒功能，如果用户发送“你好小轩“，那么将会唤醒大模型功能
            if(!this.awaken(voicePath,session.getId())){
                return;
            }
            this.sendSuccessResponse("1693640214093.mp4","processing.wav","正在处理您的请求，请耐心等待...",15d, session.getId());
            //第一步，将语音转换为文字
            String text = WebSocketServer.callPaddleSpeech.generateText(voicePath);
            logger.info("语音识别后的文本为："+text);
            this.sendSuccessResponse("1693640306073.mp4","identifiedSuccessfully.wav", "语音识别成功!当前语音转换为如下文本：" + text,45d, session.getId());
            //判断是否只是提问
            if(!this.answer(text,session.getId())){
                return;
            }
            //第二步，发送文字命令，调用“大模型”获取生成交通场景模型所需的代码
            List<String> codeList = WebSocketServer.callBigModel.generateCode(text);
            codeList = this.accumulationCode(codeList,text,session.getId());
            String codeStr = ListUtil.toString(codeList);
            logger.info("调用大模型生成Matlab代码如下：\n" + codeStr);
            this.sendSuccessResponse("1693640388885.mp4","codeGenerationSuccessful.wav","代码生成成功，调用大模型生成Matlab代码如下：\n" + codeStr,70d, session.getId());
            //第三步，调用“WebGL”渲染三维效果像素流
            WebSocketServer.callBigModel.generatePixelStream(codeList);
            logger.info("三维场景像素流生成中...");
            this.sendPixStreamResponse("1693728667826.mp4","pixelStreamGenerating.wav","三维场景像素流生成中...", 100d, session.getId());
        } catch (Exception e){
            sendErrorResponse(e.getMessage(), session.getId());
            logger.info(LoggerUtil.getLoggerStace(e));
        }
    }

    /**
     * 唤醒功能
     * @param voicePath
     * @param sessionId
     * @return
     */
    private boolean awaken(String voicePath, String sessionId){
        AjaxResult result = WebSocketServer.callPaddleSpeech.awakenCheck(voicePath);
        if(result.get(CODE_TAG).equals(HttpStatus.SUCCESS)){
            redisTemplate.opsForValue().set(sessionId, true, 30, TimeUnit.SECONDS);
            this.sendSuccessResponse("唉，我在!","",null, sessionId);
            return false;
        }else if(redisTemplate.opsForValue().get(sessionId)==null||redisTemplate.opsForValue().get(sessionId)==false){
            //FileUtils.deleteFile(voicePath);
            return false;
        }
        redisTemplate.opsForValue().set(sessionId, true, 30, TimeUnit.SECONDS);
        return true;
    }

    /**
     * 判断是否为问答
     * @param text
     * @return
     * @throws InterruptedException
     */
    public boolean answer(String text, String sessionId) throws InterruptedException {
        if(text.contains("做什么")&&text.contains("大模型")){
            Thread.sleep(8000);
            String str = "我是智慧交通大模型，能生成交通的三维数字孪生场景，譬如生成汽车、人，安装摄像头、交通信号灯，也能生成导航路线以进行直观浏览、开展自动路线调度。还能根据突发事件和应急预案，进行可视化调度等。要不请您来试用一下我的功能吧！";
            this.sendSuccessResponse("1693365009981.mp4","1693362920112.wav", str, 100d, sessionId);
            return false;
        }
        return true;
    }
    /**
     * 代码叠加功能
     * @param codeList
     * @param text
     * @param sessionId
     */
    public List<String> accumulationCode(List<String> codeList, String text, String sessionId){
        List<String> preCodes = codeMap.get(sessionId);
        if(text.contains("基础上")&&preCodes!=null){
            preCodes.addAll(codeList);
            codeList = preCodes;
        }
        codeMap.put(sessionId, codeList);
        return codeList;
    }
    /**
     * 返回执行失败响应
     * @param message 语音消息文本
     * @param sessionId socket会话id
     */
    public void sendErrorResponse(String message, String sessionId) {
        try {
            StreamSet stream = getSoundTips(message);
            AjaxResult result = AjaxResult.error(message, stream);
            WebSocketServer server = webSocketMap.get(sessionId);
            server.session.getBasicRemote().sendText(JSON.toJSONString(result));
        } catch (Exception e) {
            logger.info(LoggerUtil.getLoggerStace(e));
        }
    }

    /**
     * 返回执行成功响应
     * @param message 语音消息文本
     * @param tips 文本提示消息
     * @param sessionId socket会话id
     */
    public void sendSuccessResponse(String message, String tips, Double progress, String sessionId){
        try {
            StreamSet stream = getSoundTips(message);
            stream.setProgress(progress);
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
     * @param sessionId
     */
    public void sendPixStreamResponse(String message, Double progress, String sessionId){
        try {
            StreamSet stream = getGraphTips(message);
            stream.setProgress(progress);
            //设置任意不为空的消息，告诉前端像素流生成成功!
            stream.setScreen("SUCCESS");
            AjaxResult result = AjaxResult.success(message, stream);
            WebSocketServer server = webSocketMap.get(sessionId);
            server.session.getBasicRemote().sendText(JSON.toJSONString(result));
        } catch (Exception e) {
            logger.info(LoggerUtil.getLoggerStace(e));
        }
    }
    public void sendSuccessResponse(String graphLocation, String soundLocation, String tips, Double progress, String sessionId){
        try {
            StreamSet stream = new StreamSet();
            stream.setGraph(graphLocation);
            stream.setSound(soundLocation);
            stream.setProgress(progress);
            AjaxResult result = AjaxResult.success(tips, stream);
            WebSocketServer server = webSocketMap.get(sessionId);
            server.session.getBasicRemote().sendText(JSON.toJSONString(result));
        } catch (Exception e) {
            logger.info(LoggerUtil.getLoggerStace(e));
        }
    }
    public void sendPixStreamResponse(String graphLocation, String soundLocation, String message, Double progress, String sessionId){
        try {
            StreamSet stream = new StreamSet();
            stream.setGraph(graphLocation);
            stream.setSound(soundLocation);
            stream.setProgress(progress);
            stream.setScreen("SUCCESS");
            AjaxResult result = AjaxResult.success(message, stream);
            WebSocketServer server = webSocketMap.get(sessionId);
            server.session.getBasicRemote().sendText(JSON.toJSONString(result));
        } catch (Exception e) {
            logger.info(LoggerUtil.getLoggerStace(e));
        }
    }
    /**
     * 发送声音提示消息
     *
     * @param message
     */
    public StreamSet getSoundTips(String message) {
        StreamSet stream = new StreamSet();
        String fileName = WebSocketServer.callPaddleSpeech.generateVoice(message);
        stream.setSound(fileName);
        return stream;
    }

    /**
     * 生成视频人脸提示
     * @param message
     * @return
     */
    public StreamSet getGraphTips(String message){
        StreamSet stream = new StreamSet();
        //根据文本生成对应的视频人脸，并返回该视频对应的fid值
        String fid = WebSocketServer.callVideo.generateVideo(message);
        logger.info("fid的值为："+fid);
        //根据fid从minio中下载对应的视频
        String fileName = WebSocketServer.callMinio.download(fid);
        stream.setGraph(fileName);
        //调用paddleSpeech根据文本生成语音信息
        fileName = WebSocketServer.callPaddleSpeech.generateVoice(message);
        stream.setSound(fileName);
        return stream;
    }
}
