package com.ruoyi.simulation.call;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.simulation.util.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class CallUE4Engine {
    private final Logger logger = LoggerFactory.getLogger(CallUE4Engine.class);
    @Resource
    private Environment environment;
    private List<MessageReceiver> clientList = new ArrayList<>();
    public void initial(){
        try {
            String port = this.environment.getProperty("simulation.ue4engine.port");
            ServerSocket server = new ServerSocket(Integer.parseInt(port));
            ExecutorService pool = Executors.newFixedThreadPool(100);
            while(true){
                Socket socket = server.accept();
                logger.info("----------------------------收到UE5消息:"+socket.getLocalAddress()+"-----------------------------");
                //防止长时间没有数据传输，socket被关闭
                socket.setKeepAlive(true);
                MessageReceiver client = new MessageReceiver(socket);
                this.clientList.add(client);
                pool.submit(client);
            }
        } catch (Exception ex) {
            LoggerUtil.printLoggerStace(ex);
        }
    }
    private class MessageReceiver implements Runnable{
        private Socket socket;
        public MessageReceiver(Socket socket){
            this.socket = socket;
        }
        @Override
        public void run() {
            while(true){
                try {
                    InputStream inputStream = this.socket.getInputStream();
                    byte[] buffer = new byte[1024];
                    inputStream.read(buffer);
                    String str = new String(buffer, StandardCharsets.UTF_8);
                    logger.info("----------------------------收到UE5消息:"+str+"-----------------------------");
                    if(StringUtils.equals(str, MessageType.PING.toString())){
                        sendPING();
                    }
                } catch (Exception ex) {
                    LoggerUtil.printLoggerStace(ex);
                }
            }
        }
        /**
         * 发送心跳
         * @throws IOException
         */
        private synchronized void sendPING() throws IOException {
            this.socket.getOutputStream().write(MessageType.PING.toString().getBytes(StandardCharsets.UTF_8));
        }

        /**
         * 发送消息
         * @param message
         * @throws IOException
         */
        private synchronized void sendMessage(String message) throws IOException {
            this.socket.getOutputStream().write(message.getBytes(StandardCharsets.UTF_8));
        }
    }
    /**
     * 发送消息
     * @param message
     * @throws IOException
     */
    public synchronized void sendMessage(String message) throws IOException {
        for(MessageReceiver client: clientList){
            client.sendMessage(message);
        }
    }
    public enum MessageType {
        PING("PING");
        private final String type;
        MessageType(String type){
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }
}
