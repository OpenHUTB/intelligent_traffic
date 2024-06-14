package com.ruoyi.simulation.config;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.simulation.call.CallPaddleSpeech;
import com.ruoyi.simulation.util.FileOperatorUtil;
import com.ruoyi.simulation.util.LoggerUtil;
import com.ruoyi.simulation.util.SendResponseUtil;
import com.ruoyi.simulation.util.StreamSet.Status;
import com.ruoyi.simulation.util.VoiceUtil;
import com.ruoyi.simulation.websocket.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class ProcessVoiceListener implements ApplicationListener<ApplicationStartedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ProcessVoiceListener.class);
    private static final Map<String, String> lastTextMap = new ConcurrentHashMap<String, String>();
    @Resource
    private Environment environment;
    @Resource
    private CallPaddleSpeech callPaddleSpeech;
    @Resource
    private RedisTemplate<String,Boolean> redisTemplate;
    @Resource
    private FileOperatorUtil fileUtil;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent e) {
        Thread processVoice = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    processVoice();
                }
            }
        });
        processVoice.start();
    }
    private void processVoice(){
        try {
            //从队列中去除元素，若队列为空，则阻塞
            VoiceUtil element = WebSocketServer.voiceCommandQueue.take();
            String voicePath = element.getVoice();
            String sessionId = element.getSessionId();
            //第一步，将语音转换为文字
            long start = System.currentTimeMillis();
            String text = this.callPaddleSpeech.generateText(voicePath);
            long end = System.currentTimeMillis();
            logger.info("解析语音时间："+(end-start));
            logger.info("解析语音为："+text);
            logger.info("语音队列长度:"+WebSocketServer.voiceCommandQueue.size());

            //语音唤醒功能，如果用户发送“你好小轩“，那么将会唤醒系统功能
            Boolean flag = redisTemplate.opsForValue().get(sessionId);
            if (!StringUtils.isEmpty(text)){
                if (text.contains("你好")&&(text.contains("小轩")||text.contains("晓轩")
                        ||text.contains("小萱")||text.contains("小宣")||text.contains("晓萱")
                        ||text.contains("小瑄"))){
                    WebSocketServer.voiceSuperpositionMap.get(sessionId).clear();
                    WebSocketServer.voiceCommandQueue.clear();
                    //将状态修改为唤醒状态
                    this.redisTemplate.opsForValue().set(sessionId, true, 15, TimeUnit.SECONDS);
                    SendResponseUtil.sendSoundResponse(Status.AWAKENED,"reply.wav",null,sessionId);
                    fileUtil.deleteExpiredFiles(sessionId);
                } else if(flag!=null&&flag){
                    //存储解析的文本备用，当下一次文本和本次文本解析内容相同，则认为解析完成
                    String command = lastTextMap.get(sessionId);
                    if(!StringUtils.isEmpty(command)&&command.endsWith(text)&&command.length()>=4){
                        logger.info("-------------------------------------------------------------------------------------");
                        logger.info("解析语音为："+command);
                        logger.info("-------------------------------------------------------------------------------------");
                        lastTextMap.clear();
                        element.setCommand(command);
                        ProcessCommandListener.readingQueue.put(element);
                        WebSocketServer.voiceSuperpositionMap.get(sessionId).clear();
                        WebSocketServer.voiceCommandQueue.clear();
                        this.redisTemplate.delete(sessionId);
                        fileUtil.deleteExpiredFiles(sessionId);
                        SendResponseUtil.sendSoundResponse(Status.WAITING,"identifiedSuccessfully.wav", "语音识别成功!当前语音转换为如下文本：" + command, sessionId);
                    }else{
                        lastTextMap.put(sessionId,text);
                    }
                }
            }
        } catch (InterruptedException e) {
            LoggerUtil.printLoggerStace(e);
        }
    }
}
