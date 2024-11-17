package com.ruoyi.simulation.call;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.simulation.util.*;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * MQTT配置工具类
 */
@Configuration
public class MqttConsumerClient {
    private static final Logger logger = LoggerFactory.getLogger(MqttConsumerClient.class);
    @Resource
    private Environment environment;
    private MqttClient client;
    /**
     * 临时存储MQTT车辆信息的阻塞队列
     */
    public static final LinkedBlockingQueue<TimeBucket> mqttQueue = new LinkedBlockingQueue<TimeBucket>(2000);
    public static final Map<Long, List<JSONObject>> timeDomainMap = new HashMap<Long, List<JSONObject>>(2000);
    public void connect(){
        try {
            //MQTT服务端地址
            String hostURL = environment.getProperty("spring.mqtt.url");
            //客户端id
            String clientId = environment.getProperty("spring.mqtt.client.id");
            client = new MqttClient(hostURL, clientId, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            //是否清空session，设置为false表示服务器会保留客户端的连接记录，客户端重连之后能获取到服务器在客户端断开连接期间推送的消息。若设置为true，则表示每次都是以新的身份连接服务器
            options.setCleanSession(true);
            options.setAutomaticReconnect(true);
            //连接MQTT的用户名
            String username = environment.getProperty("spring.mqtt.username");
            options.setUserName(username);
            //连接MQTT的密码
            String password = environment.getProperty("spring.mqtt.password");
            options.setPassword(password.toCharArray());
            //连接MQTT的主题
            String topic = environment.getProperty("spring.mqtt.default.topic");
            options.setConnectionTimeout(100);
            options.setKeepAliveInterval(20);
            options.setWill(topic, (clientId + "与服务器断开连接").getBytes(),0,false);
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                    logger.error("与服务器断开连接，可重连");
                    LoggerUtil.printLoggerStace(throwable);
                    while(!client.isConnected()){
                        try {
                            Thread.sleep(5000);
                            logger.info("mqtt连接重建中...");
                            client.connect();
                        } catch (Exception e) {
                            LoggerUtil.printLoggerStace(e);
                        }
                    }
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    //logger.info("----------------------------------来自mqtt服务端消息----------------------------------");
                    try{
                        if(message==null|| message.getPayload()==null){
                            return;
                        }
                        JSONObject response = JSON.parseObject(new String(message.getPayload()));
                        //获取当前时间
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss.SSS");
                        String timeStr = response.getString("timestamp");
                        Date currentTime = sdf.parse(timeStr);
                        long timestamp = currentTime.getTime();
                        long timeDomain = TimeBucket.getTimeDomain(timestamp);
                        if(!timeDomainMap.containsKey(timeDomain)){
                            List<JSONObject> responseList = new ArrayList<JSONObject>();
                            timeDomainMap.put(timeDomain, responseList);
                            TimeBucket bucket = new TimeBucket();
                            bucket.setTimeDomain(timeDomain);
                            bucket.setResponseList(responseList);
                            mqttQueue.put(bucket);
                        }
                        List<JSONObject> responseList = timeDomainMap.get(timeDomain);
                        responseList.add(response);
                    }catch (Exception e){
                        LoggerUtil.printLoggerStace(e);
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });
            client.connect(options);
            client.subscribe(topic);
        } catch (MqttException e) {
            LoggerUtil.printLoggerStace(e);
        }
    }

    /**
     * 关闭mqtt服务
     */
    public void close(){
        try {
            this.client.disconnect();
        } catch (MqttException e) {
            LoggerUtil.printLoggerStace(e);
        }
    }
}
