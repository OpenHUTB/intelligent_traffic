package com.ruoyi.simulation.util;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.simulation.websocket.WebSocketServer;

import javax.websocket.Session;
import com.ruoyi.simulation.util.StreamSet.Status;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回响应的工具类
 */
public class SendResponseUtil {
    /**
     * 返回响应，表示正在处理用户的命令
     * @param message
     * @param sessionId
     */
    public static synchronized void sendHandleResponse(String message, String sessionId){
        try {
            if(message.contains("请")){
                message = message.replaceAll("请","");
            }
            StreamSet stream = new StreamSet();
            stream.setMessage("正在为您"+message);
            WebSocketServer server = WebSocketServer.webSocketMap.get(sessionId);
            Session session = server.getSession();
            session.getBasicRemote().sendText(JSON.toJSONString(stream));
        } catch (Exception e) {
            LoggerUtil.printLoggerStace(e);
        }
    }

    /**
     * 返回交通指数信息
     * @param indirectionMap
     * @param sessionId
     */
    public static synchronized void sendIndirectionResponse(Map<String,Object> indirectionMap, String sessionId){
        try {
            StreamSet stream = new StreamSet();
            stream.setIndirectionMap(indirectionMap);
            WebSocketServer server = WebSocketServer.webSocketMap.get(sessionId);
            Session session = server.getSession();
            session.getBasicRemote().sendText(JSON.toJSONString(stream));
        } catch (Exception e) {
            LoggerUtil.printLoggerStace(e);
        }
    }
    /**
     * 返回异常提示响应
     * @param e
     * @param sessionId
     */
    public static synchronized void sendErrorResponse(Exception e, String sessionId){
        try {
            String message = "系统异常!";
            if(e instanceof CustomizedException){
                message = e.getMessage();
            }
            StreamSet stream = new StreamSet();
            stream.setMessage(message+"请联系管理员。");
            WebSocketServer server = WebSocketServer.webSocketMap.get(sessionId);
            Session session = server.getSession();
            session.getBasicRemote().sendText(JSON.toJSONString(stream));
        } catch (Exception ex) {
            LoggerUtil.printLoggerStace(ex);
        }
    }

    /**
     * 返回对话响应
     * @param message
     * @param sessionId
     */
    public static synchronized void sendDialogueResponse(String message, String sessionId){
        try {
            StreamSet stream = new StreamSet();
            stream.setMessage(message);
            WebSocketServer server = WebSocketServer.webSocketMap.get(sessionId);
            Session session = server.getSession();
            session.getBasicRemote().sendText(JSON.toJSONString(stream));
        } catch (Exception e) {
            LoggerUtil.printLoggerStace(e);
        }
    }

    /**
     * 返回像素流信息
     * @param instruction
     * @param sessionId
     */
    public static synchronized void sendFinalResponse(String instruction, String sessionId){
        try {
            StreamSet stream = new StreamSet();
            //数字仿真
            if(instruction.contains("display_a_frame.py")){
                stream.setSignal(StreamSet.Signal.DIGITAL_SIMULATION.toString());
            }else if(instruction.contains("control_traffic_light_test.py")){
                //设置红绿灯
                // String codeStr = "control_traffic_light_test.py --color id 1 --color time 60";
                stream.setSignal(StreamSet.Signal.TRAFFIC_LIGHT_INSTRUCTION.toString());
                //设置红绿灯参数
                String prefix = "--color_id";
                String suffix = "--color_time";
                String serial = instruction.substring(instruction.indexOf(prefix)+prefix.length(), instruction.indexOf(suffix));
                String second = instruction.substring(instruction.indexOf(suffix)+suffix.length());
                Map<String,Object> lightInfo = new HashMap<String,Object>();
                lightInfo.put("serial",serial);
                lightInfo.put("second",second);
                lightInfo.put("roadLength",80);
                lightInfo.put("carCount", 0);
                lightInfo.put("congestion", 0);
                lightInfo.put("waitTime",0);
                if(WebSocketServer.carCountMap.get(sessionId)!=null&&WebSocketServer.carCountMap.get(sessionId)!=0){
                    int carCount = WebSocketServer.carCountMap.get(sessionId);
                    lightInfo.put("carCount", carCount);
                    int congestion = (int)(Math.random()*(carCount+39)/40);
                    lightInfo.put("congestion", congestion);
                    int waitTime = 20+(int)(Math.random()*(carCount+39)/40*20);
                    lightInfo.put("waitTime",waitTime);
                }
                stream.setLightInfo(lightInfo);
            }else if(instruction.endsWith("Carla_control_G29.py")){
                Thread.sleep(35000);
                stream.setMessage("自动驾驶即将结束，请手动接管方向盘!");
            }else if(!instruction.contains("change_weather.py")&&!instruction.contains("generate_traffic.py")){
                stream.setSignal(StreamSet.Signal.ORDINARY.toString());
            }
            //通过websocket向前端发送数据
            WebSocketServer server = WebSocketServer.webSocketMap.get(sessionId);
            Session session = server.getSession();
            session.getBasicRemote().sendText(JSON.toJSONString(stream));
        } catch (Exception e) {
            LoggerUtil.printLoggerStace(e);
        }
    }

    /**
     * 返回语音响应
     * @param status
     * @param soundLocation
     * @param message
     * @param sessionId
     */
    public static synchronized void sendSoundResponse(Status status, String soundLocation, String message, String sessionId){
        try {
            StreamSet stream = new StreamSet();
            stream.setStatus(status.toString());
            stream.setMessage(message);
            WebSocketServer server = WebSocketServer.webSocketMap.get(sessionId);
            Session session = server.getSession();
            session.getBasicRemote().sendText(JSON.toJSONString(stream));
        } catch (Exception e) {
            LoggerUtil.printLoggerStace(e);
        }
    }
}
