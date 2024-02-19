package com.ruoyi.simulation.config;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.simulation.call.CallPaddleSpeech;
import com.ruoyi.simulation.call.CallUE4Engine;
import com.ruoyi.simulation.call.CallWizardCoder;
import com.ruoyi.simulation.util.*;
import com.ruoyi.simulation.websocket.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class ProcessCommandListener implements ApplicationListener<ApplicationStartedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    public static final LinkedBlockingQueue<VoiceUtil> readingQueue = new LinkedBlockingQueue<VoiceUtil>(5);

    @Resource
    private ProcessOperationUtil processUtil;
    @Resource
    private CallWizardCoder callWizardCoder;
    @Resource
    private CallUE4Engine callUE4Engine;
    @Resource
    RedisTemplate<Object, Object> redisTemplate;
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
            long start = System.currentTimeMillis();
            List<String> codeList = this.callWizardCoder.generateCode(command);
            String codeStr = ListUtil.toString(codeList);
            logger.info("调用WizardCoder生成代码如下：\n" + codeStr);
            long end = System.currentTimeMillis();
            logger.info("调用WizardCoder耗费时间：" + (end-start));
            start = System.currentTimeMillis();
            //根据指令以及进程id杀死指定进程
            processUtil.killProcess(codeStr);
            end = System.currentTimeMillis();
            logger.info("杀死互斥进程耗费时间：" + (end-start));
            //第三步，调用“UE4”渲染三维效果像素流
            codeStr = this.executeCommand(codeStr,sessionId);
            SendResponseUtil.sendFinalResponse(codeStr, sessionId);
        } catch (Exception e) {
            LoggerUtil.printLoggerStace(e);
            if(!StringUtils.isEmpty(sessionId)){
                SendResponseUtil.sendErrorResponse(e, sessionId);
            }
        }
    }
    /**
     * 执行附加进程
     * @param codeStr
     */
    private String executeCommand(String codeStr, String sessionId) {
        Set<String> commandList = WebSocketServer.webSocketCommandMap.get(sessionId);
        //当第二次进入相应场景时，则不隐藏建筑
        if(codeStr.contains("get_maps.py --mapname")){
            String mapName = codeStr.replace("get_maps.py --mapname","").trim();
            if(commandList.contains(mapName+" get_buildings.py")){
                codeStr = codeStr.replace("get_maps.py","get_mapsfast.py");
            }
        }
        //记录下每个场景的添加建筑命令
        if(StringUtils.equals(codeStr,"get_buildings.py")){
            String mapName = this.callWizardCoder.getMapName("get_mapname.py");
            if(!StringUtils.isEmpty(mapName)&&mapName.contains("/")){
                mapName = mapName.substring(mapName.lastIndexOf("/")+1);
            }
            commandList.add(mapName+" get_buildings.py");
        }
        //执行驾驶进程
        if(StringUtils.equals(codeStr, "control_steeringwheel.py")||StringUtils.equals(codeStr, "automatic_control_steeringwheel.py")
                ||StringUtils.equals(codeStr,"automatic_control_revised.py")){
            codeStr = "Carla_control_G29.py";
        }
        if(codeStr.contains("generate_traffic.py --number-of-vehicles")){
            int add = Integer.parseInt(codeStr.replace("generate_traffic.py --number-of-vehicles","").trim());
            int count = 0;
            if(WebSocketServer.carCountMap.containsKey(sessionId)){
                count = WebSocketServer.carCountMap.get(sessionId);
            }
            WebSocketServer.carCountMap.put(sessionId, count+add);
        }
        logger.info(codeStr);
        this.callWizardCoder.executeExample(codeStr);
        return codeStr;
    }
    /**
     * 判断是否为问答
     * @param text
     * @return
     * @throws InterruptedException
     */
    public boolean answer(String text, String sessionId) {
        if((text.contains("做什么")||text.contains("干什么")||text.contains("有什么"))&&text.contains("大模型")){
            String message = "我是智慧交通大模型，能生成交通的三维数字孪生场景，譬如生成汽车、人，安装摄像头、交通信号灯，也能生成导航路线以进行直观浏览、开展自动路线调度。还能根据突发事件和应急预案，进行可视化调度等。要不请您来试用一下我的功能吧！";
            SendResponseUtil.sendDialogueResponse(message, sessionId);
            return false;
        }
        return true;
    }
}
