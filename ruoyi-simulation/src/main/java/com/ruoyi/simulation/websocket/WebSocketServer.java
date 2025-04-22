package com.ruoyi.simulation.websocket;

import com.ruoyi.simulation.util.ElementUtil;
import com.ruoyi.simulation.util.FileOperatorUtil;
import com.ruoyi.simulation.util.LoggerUtil;
import com.ruoyi.simulation.util.VoiceUtil;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

@Component
@ServerEndpoint("/simulation/websocket/")
@CrossOrigin
@Getter
public class WebSocketServer {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    public static final Map<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<String, WebSocketServer>();
    public static final Map<String, Set<String>> webSocketCommandMap = new ConcurrentHashMap<String, Set<String>>();
    public static final LinkedBlockingQueue<VoiceUtil> voiceCommandQueue = new LinkedBlockingQueue<VoiceUtil>(50);
    public static final LinkedBlockingQueue<ElementUtil> commandQueue = new LinkedBlockingQueue<ElementUtil>(50);
    public static final Map <String, LinkedList<VoiceUtil>> voiceSuperpositionMap = new ConcurrentHashMap<String,LinkedList<VoiceUtil>>();
    private static FileOperatorUtil fileUtil;
    /**
     * 参考坐标系的坐标原点经度坐标
     */
    public static double coordinateOriginLongitude = 113.6544;
    /**
     * 参考坐标系的坐标原点纬度坐标
     */
    public static double coordinateOriginLatitude = 27.684011;
    private Session session = null;
    /**
     * 打开连接的回调函数
     *
     * @param session
     */
    @OnOpen
    public void openConnection(Session session) {
        this.session = session;
        webSocketMap.put(session.getId(), this);
        webSocketCommandMap.put(session.getId(),new HashSet<String>());
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
        webSocketCommandMap.remove(session.getId());
        logger.info("----------------------------连接已关闭-----------------------------");
    }

    /**
     * 收到客户端消息时的回调函数
     *
     * @param command    命令
     * @param session
     */
    @OnMessage
    public void onMessage(String command, Session session) {
        logger.info("----------------------------收到消息-----------------------------");
        logger.info(command);
        try {
            ElementUtil element =  new ElementUtil();
            if(command.contains("执行优先")){
                command = command.replace("执行优先","直行优先");
            }
            element.setCommand(command);
            element.setSessionId(session.getId());
            commandQueue.put(element);
        } catch (InterruptedException e) {
            LoggerUtil.printLoggerStace(e);
        }
    }
    /**
     * 收到客户端消息时的回调函数
     *
     * @param blob    语音命令
     * @param session
     */
    public void onMessage(byte[] blob, Session session) {
        try{
            //将字节数组中的数据写入到ByteArrayOutputStream中，以便能随时复制
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            byteStream.write(blob);
            byteStream.flush();
            //将接收到的语音文件数据流叠加存储到磁盘中
            LinkedList<VoiceUtil> voiceSuperpositionList = addSuperposition(session.getId(), byteStream);
            this.addCommand(voiceSuperpositionList);
        } catch (Exception e){
            LoggerUtil.printLoggerStace(e);
        }
    }

    /**
     * 将接收到的语音文件数据流叠加存储到磁盘中
     * @param sessionId
     * @param byteStream
     * @return
     * @throws IOException
     */
    public LinkedList<VoiceUtil> addSuperposition(String sessionId, ByteArrayOutputStream  byteStream) throws IOException, UnsupportedAudioFileException {
        LinkedList<VoiceUtil> voiceSuperpositionList = voiceSuperpositionMap.get(sessionId);
        Iterator<VoiceUtil> iterator = voiceSuperpositionList.iterator();
        do{
            if(!iterator.hasNext()){
                //存储话筒接收到的语音文件，并返回语音文件存储位置的绝对路径
                String absolutePath = fileUtil.storeFileToDisk(sessionId, new ByteArrayInputStream(byteStream.toByteArray()),"wav");
                //logger.info("语音文件存储路径："+absolutePath);
                voiceSuperpositionList.add(new VoiceUtil(sessionId, absolutePath,1,null));
                break;
            }
            VoiceUtil voice = iterator.next();
            if(voice.getCount()>= VoiceUtil.MAX_COUNT){
                continue;
            }
            //获取列表中已经存在的文件信息，并将语音叠加到该文件中
            String absolutePath = voice.getVoice();
            absolutePath = fileUtil.appendWav(sessionId, new File(absolutePath), byteStream.toByteArray());
            voice.setVoice(absolutePath);
            voice.setCount(voice.getCount()+1);
            if(voice.getCount()==2){
                //存储话筒接收到的语音文件，并返回语音文件存储位置的绝对路径
                absolutePath = fileUtil.storeFileToDisk(sessionId, new ByteArrayInputStream(byteStream.toByteArray()),"wav");
                //logger.info("语音文件存储路径："+absolutePath);
                voiceSuperpositionList.add(new VoiceUtil(sessionId,absolutePath,1,null));
                break;
            }
        }while (iterator.hasNext());
        return voiceSuperpositionList;
    }

    /**
     * 从叠加队列中获取队首元素
     * @param voiceSuperpositionList
     * @throws InterruptedException
     */
    public void addCommand(LinkedList<VoiceUtil> voiceSuperpositionList) throws InterruptedException {
        VoiceUtil voice = voiceSuperpositionList.element();
        if(voice.getCount()>= VoiceUtil.MAX_COUNT){
            voiceSuperpositionList.remove(voice);
        }
        //向队列末尾添加元素，若队列已满则阻塞
        voiceCommandQueue.put(voice);
    }
    public static void getConsoleCommand(){
        while(true){
            try{
                ElementUtil element = new ElementUtil();
                Scanner input = new Scanner(System.in);
                System.out.println("请输入文本命令:");
                element.setCommand(input.next());
                List<String> sessionIdList = new ArrayList<String>(webSocketMap.keySet());
                element.setSessionId(sessionIdList.get(0));
                commandQueue.put(element);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
