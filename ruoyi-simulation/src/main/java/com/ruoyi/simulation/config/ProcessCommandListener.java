package com.ruoyi.simulation.config;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.simulation.call.CallWizardCoder;
import com.ruoyi.simulation.util.*;
import com.ruoyi.simulation.websocket.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class ProcessCommandListener implements ApplicationListener<ApplicationStartedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    public static final LinkedBlockingQueue<VoiceUtil> readingQueue = new LinkedBlockingQueue<VoiceUtil>(5);
    @Resource
    private ProcessOperationUtil processUtil;
    @Resource
    private CallWizardCoder callWizardCoder;
    @Override
    public void onApplicationEvent(ApplicationStartedEvent e) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    processCommand();
                }
            }
        });
        thread.start();
    }

    /**
     * 处理语音请求
     */
    private void processCommand() {
        String sessionId = null;
        try {
            //从队列中去除元素，若队列为空，则阻塞
            logger.info("语音队列长度："+WebSocketServer.commandQueue.size());
            ElementUtil element = WebSocketServer.commandQueue.take();
            String command = element.getCommand();
            sessionId = element.getSessionId();
            //判断是否只是提问
            if(!answer(command,sessionId)){
                return;
            }
            SendResponseUtil.sendHandleResponse(command, sessionId);
            //第二步，发送文字命令，调用“大模型”获取生成交通场景模型所需的代码
            List<String> codeList = this.callWizardCoder.generateCode(command);
            String codeStr = ListUtil.toString(codeList);
            logger.info("调用WizardCoder生成代码如下：\n" + codeStr);
            //根据指令以及进程id杀死指定进程
            processUtil.killProcess(codeStr);
            //第三步，调用“UE4”渲染三维效果像素流
            this.callWizardCoder.executeExample(codeStr);
            logger.info(codeStr);
            SendResponseUtil.sendFinalResponse(codeStr, sessionId);
        } catch (Exception e) {
            LoggerUtil.printLoggerStace(e);
            if(!StringUtils.isEmpty(sessionId)){
                SendResponseUtil.sendErrorResponse(e, sessionId);
            }
        }
    }
    /**
     * 判断是否为问答
     * @param text
     * @return
     * @throws InterruptedException
     */
    public boolean answer(String text, String sessionId) {
        if((text.contains("做什么")||text.contains("干什么"))&&text.contains("大模型")){
            String message = "我是智慧交通大模型，能生成交通的三维数字孪生场景，譬如生成汽车、人，安装摄像头、交通信号灯，也能生成导航路线以进行直观浏览、开展自动路线调度。还能根据突发事件和应急预案，进行可视化调度等。要不请您来试用一下我的功能吧！";
            SendResponseUtil.sendDialogueResponse(message, sessionId);
            return false;
        }
        return true;
    }
}
