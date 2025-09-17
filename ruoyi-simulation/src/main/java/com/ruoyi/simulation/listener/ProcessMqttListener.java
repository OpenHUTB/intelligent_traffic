package com.ruoyi.simulation.listener;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.ruoyi.simulation.call.CallRotationMatrix;
import com.ruoyi.simulation.call.MqttConsumerClient;
import com.ruoyi.simulation.domain.LocationState;
import com.ruoyi.simulation.domain.Radar;
import com.ruoyi.simulation.domain.Vehicle;
import com.ruoyi.simulation.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 处理车辆轨迹监听器
 */
//@Component
public class ProcessMqttListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(ProcessMqttListener.class);
    @Resource
    private MqttConsumerClient client;
    @Resource
    private Environment environment;
    @Resource
    private CallRotationMatrix callRotationMatrix;
    /**
     * 车辆及其坐标信息
     */
    public static List<Vehicle> vehicleLocationList = new ArrayList<Vehicle>();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            client.connect();
            Thread.sleep(TimeBucket.period);
            processMqttResponse();
        } catch (Exception e) {
            LoggerUtil.printLoggerStace(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        client.close();
    }

    /**
     * 处理队列中的mqtt响应数据
     */
    public void processMqttResponse() throws InterruptedException, IOException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss.SSS");
        while(true){
            Thread.sleep(TimeBucket.period);
            TimeBucket bucket = MqttConsumerClient.mqttQueue.take();
            //每次取出一个时间域的MQTT数据，则将相应地将其从timeDomainMap中删除
            MqttConsumerClient.timeDomainMap.remove(bucket.getTimeDomain());
            List<JSONObject> responseList = bucket.getResponseList();
            //雷达监测到的车辆数据
            Map<String,Radar> radarMap = new HashMap<String,Radar>();
            for(JSONObject response: responseList){
                Radar radar = this.initialRadar(response, radarMap);
                //获取当前时间
                String timeStr = response.getString("timestamp");
                Date currentTime = sdf.parse(timeStr);
                //获取所有车辆信息
                JSONArray objectList = response.getJSONArray("objects_list");
                for(int i=0;i<objectList.size();i++){
                    JSONObject object = objectList.getJSONObject(i);
                    //初始化车辆信息
                    Vehicle vehicle = this.initialVehicle(object, radar);
                    //初始化车辆瞬时状态信息
                    LocationState state = this.initialLocationState(object, currentTime, vehicle, radar);
                    List<LocationState> stateList = vehicle.getStateList();
                    stateList.add(state);
                }
            }
            this.writeRadarLocation(radarMap.values());
            //List<Vehicle> vehicleList = this.mergeVehicleLocationState(radarMap);
            //this.writeCarlaLocation(vehicleList);
        }
    }
    /**
     * 初始化雷达信息
     * @param response
     * @param radarMap
     * return
     */
    public Radar initialRadar(JSONObject response, Map<String,Radar> radarMap){
        //雷达设备编号
        String deviceNum = response.getString("device_num");
        if(!radarMap.containsKey(deviceNum)){
            Radar radar = new Radar();
            //设置雷达设备编号
            radar.setDeviceNum(deviceNum);
            //获取雷达安装位置
            String location = response.getString("location");
            radar.setLocation(location);
            //获取雷达经度
            Double longitude = response.getDouble("longitude");
            radar.setLongitude(longitude);
            //获取雷达纬度
            Double latitude = response.getDouble("latitude");
            radar.setLatitude(latitude);
            //获取雷达海拔
            Double altitude = response.getDouble("altitude");
            radar.setAltitude(altitude);
            radarMap.put(deviceNum, radar);
        }
        return radarMap.get(deviceNum);
    }

    /**
     * 初始化车辆信息
     * @param object
     * @param radar
     * @return
     */
    private Vehicle initialVehicle(JSONObject object, Radar radar){
        //获取车牌号
        String plate = object.getString("plate");
        //获取车辆id
        long vehicleId = object.getLong("ID");
        if(!StringUtil.isEmpty(plate)){
            //如果车牌号不为空，则保存车辆id和车牌号的映射关系
            PlateTransferUtil.put(radar.getDeviceNum(),vehicleId, plate);
        }else{
            //如果车牌号为空，则从车辆id和车牌的映射关系中获取车牌号
            plate = PlateTransferUtil.getPlate(radar.getDeviceNum(),vehicleId);
        }
        Map<Long, Vehicle> vehicleMap = radar.getVehicleMap();
        if(!vehicleMap.containsKey(vehicleId)){
            Vehicle vehicle = new Vehicle();
            //设置车辆id
            vehicle.setVehicleId(vehicleId);
            //获取车辆的长、宽、高，单位cm
            double length = object.getDouble("length");
            double width = object.getDouble("width");
            double height = object.getDouble("heigh");
            //设置车辆的长、宽、高，单位m
            vehicle.setLength(length/100);
            vehicle.setWidth(width/100);
            vehicle.setHeight(height/100);
            //获取车辆大小类型：0-小型车, 1-中型车, 2-大型车
            int vehicleSize = object.getInteger("type");
            //设置车辆大小类型：SMALL-小型车,MEDIUM-中型车,LARGE-大型车
            VehicleTransferUtil.transferVehicleSize(vehicleSize, vehicle);
            //获取车辆类型：0小型客车,1中型客车,2大型客车,3小型货车,4中型货车,5重型货车,6警车,7救护车,8消防车,9校车,10工程车,11危化品车,
            //12三轮车,13摩托车,14自行车,15行人,16动物,17工作人员,100其他
            int vehicleType = object.getInteger("class");
            //设置车辆类型
            VehicleTransferUtil.transferVehicleType(vehicleType, vehicle);
            //获取车身颜色：0白,1灰,2黄,3粉,4红,5紫,6绿,7蓝,8棕,9黑,100其他
            int color = object.getInteger("color");
            //设置车身颜色:WHITE-白,GRAY-灰,YELLOW-黄,PINK-粉色,RED-红,PURPLE-紫,GREEN-绿,BLUE-蓝,BROWN-棕,BLACK-黑,OTHER-其他
            VehicleTransferUtil.transferVehicleColor(color,vehicle);
            //设置车牌号
            vehicle.setPlate(plate);
            vehicleMap.put(vehicleId,vehicle);
        }
        return vehicleMap.get(vehicleId);
    }

    /**
     * 初始化车辆瞬时状态
     * @param object MQTT数据对象
     * @param currentTime MQTT时间戳
     * @param radar 雷达
     */
    private LocationState initialLocationState(JSONObject object, Date currentTime, Vehicle vehicle, Radar radar){
        LocationState state = new LocationState();
        //获取车辆相对于雷达安装位置的横向坐标，右为正，左为负，单位cm
        double positionX = object.getDouble("x");
        //设置车辆相对于雷达安装位置的横向坐标，右为正，左为负，单位m
        state.setPositionX(positionX/100);
        //获取车辆相对于雷达安装位置的纵向坐标，前为正，后为负，单位cm
        double positionY = object.getDouble("y");
        //设置车辆相对于雷达安装位置的纵向坐标，前为正，后为负，单位m
        state.setPositionY(positionY/100);
        //获取车辆相对于雷达安装位置的横向速度，右为正，左为负，单位cm/s
        double velocityX = object.getDouble("v_x");
        //设置车辆相对于雷达安装位置的横向速度，右为正，左为负，单位m/s
        state.setVelocityX(velocityX/100);
        //获取车辆相对于雷达安装位置的横向速度，右为正，左为负，单位cm/s
        double velocityY = object.getDouble("v_y");
        //设置车辆相对于雷达安装位置的纵向速度，前为正，后为负，单位m/s
        state.setVelocityY(velocityY/100);
        //获取车辆所在车道
        int lane = object.getInteger("lane");
        state.setLane(lane);
        //获取车辆的经纬度及海拔
        double longitude = object.getDouble("longitude");
        double latitude = object.getDouble("latitude");
        double altitude = object.getDouble("altitude");
        //设置车辆的经纬度及海拔
        state.setLongitude(longitude);
        state.setLatitude(latitude);
        state.setAltitude(altitude);
        //获取车辆的速度大小：单位km/h
        double speed = object.getDouble("speed");
        state.setSpeed(speed);
        //获取车辆的速度航向角：浮点类型，单位度，正北极偏转角(0~359.999度), 顺时针为正
        double heading = object.getDouble("heading");
        state.setHeading(heading);
        //获取事件类型：0正常，1实线变道，2超低速，3超高速，4占用应急车道，5异常停车，6逆行，7闯红灯，8直行道左转，9直行道右转，10左转道直行，
        //11左转道右转，12违法掉头，13机动车占用非机动车道，14非机动车占用机动车道，15事故，16行人闯入，17非机动车闯入
        int eventType = object.getInteger("event");
        //设置事件类型：NORMAL-正常, SOLID_LANE_CHANGE-实线变道, ULTRA_LOW_SPEED-超低速, ULTRA_HIGH_SPEED-超高速，
        //OCCUPYING_EMERGENCY_LANE-占用应急车道, ABNORMAL_PARKING-异常停车，OPPOSITE_DIRECTION-逆行，
        //RUN_RED_RIGHT-闯红灯, TURN_LEFT_ON_STRAIGHT-直行道左转，TURN_RIGHT_ON_STRAIGHT-直行道右转，
        //TURN_STRAIGHT_ON_LEFT-左转道直行,TURN_RIGHT_ON_LEFT-左转道右转, ILLEGAL_TURN-违法掉头, OCCUPYING_NON_MOTOR-机动车占用非机动车道，
        //NON_MOTOR_OCCUPYING-非机动车占用机动车道, ACCIDENT-事故，PEDESTRIAN_ENTERING-行人闯入，NON_MOTOR_ENTERING-非机动车闯入
        LocationTransferUtil.transferEventType(eventType, state);
        state.setCreateTime(currentTime);
        double timestamp = TimestampUtil.getCustomerTime(currentTime);
        state.setTimestamp(timestamp);
        state.setRadar(radar);
        state.setVehicle(vehicle);
        return state;
    }
    /**
     * 合并多个雷达监测到的车辆轨迹
     */
    private List<Vehicle> mergeVehicleLocationState(Map<String, Radar> radarMap) {
        Map<String,Vehicle> tempMap = new HashMap<String,Vehicle>();
        //遍历雷达信息，radarMap的key为雷达设备编码deviceNum
        for(Radar radar: radarMap.values()){
            //获取雷达监测到的车辆信息，vehicleMap的key为车辆id
            Map<Long, Vehicle> vehicleMap = radar.getVehicleMap();
            //遍历某个雷达中的车辆信息
            for(Vehicle v: vehicleMap.values()){
                String plate = v.getPlate();
                if(StringUtils.isEmpty(plate)){
                    continue;
                }
                if(!tempMap.containsKey(plate)){
                    tempMap.put(plate, v);
                }else{
                    //如果车辆集合中已经包含了该车辆信息，则只需要将轨迹添加到车辆中即可
                    Vehicle vehicle = tempMap.get(plate);
                    List<LocationState> stateList = vehicle.getStateList();
                    stateList.addAll(v.getStateList());
                }
            }
        }
        //对车辆轨迹坐标进行时间戳排序
        for(Vehicle vehicle: tempMap.values()){
            List<LocationState> stateList = vehicle.getStateList();
            stateList.sort(new Comparator<LocationState>() {
                @Override
                public int compare(LocationState o1, LocationState o2) {
                    return o1.getCreateTime().compareTo(o2.getCreateTime());
                }
            });
            this.callRotationMatrix.transferMatrixLocation(stateList);
        }
        return new ArrayList<Vehicle>(tempMap.values());
    }
    /**
     * 将车辆坐标生成txt文件
     * @param vehicleList
     * @throws IOException
     */
    private void writeCarlaLocation(List<Vehicle> vehicleList) throws IOException {
        //指定车辆运行轨迹的txt文件的存储目录
        String vehiclePath = this.environment.getProperty("simulation.filepath.vehiclePath");
        File directory = new File(vehiclePath+"/vehicle/");
        if(!directory.exists()){
            directory.mkdirs();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        List<LocationState> stateList = new ArrayList<>();
        //往文本文件中写入车辆数据
        for(Vehicle vehicle: vehicleList){
            stateList.addAll(vehicle.getStateList());
        }
        stateList.sort(new Comparator<LocationState>() {
            @Override
            public int compare(LocationState o1, LocationState o2) {
                return o1.getCreateTime().compareTo(o2.getCreateTime());
            }
        });
        BufferedWriter bufferedWriter = null;
        try{
            String fileName = "locationState.txt";
            File file = new File(directory, fileName);
            FileOutputStream outputStream = new FileOutputStream(file,true);
            OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);
            bufferedWriter = new BufferedWriter(streamWriter);
            for(LocationState state: stateList){
                Radar radar = state.getRadar();
                String str = state.getVehicle().getVehicleId()+", "+state.getRotationX()+", "+state.getRotationY()+", "+state.getHeading()+", "+state.getTimestamp()+", ["+ state.getLongitude()+","+state.getLatitude()+"]";
                bufferedWriter.write(str);
                bufferedWriter.newLine();
            }
        }finally {
            if(bufferedWriter!=null){
                bufferedWriter.close();
            }
        }
    }

    /**
     * 输出基于雷达的车辆坐标
     * @param radarList
     * @throws IOException
     */
    private void writeRadarLocation(Collection<Radar> radarList) throws IOException {
        String vehiclePath = this.environment.getProperty("simulation.filepath.vehiclePath");
        //指定车辆运行轨迹的txt文件的存储目录
        File directory = new File(vehiclePath+"/radar/");
        if(!directory.exists()){
            directory.mkdirs();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        //基于不同的雷达往文本文件中写入车辆数据
        for(Radar radar: radarList){
            BufferedWriter bufferedWriter = null;
            try{
                List<LocationState> stateList = new ArrayList<LocationState>();
                for(Vehicle vehicle: radar.getVehicleMap().values()){
                    this.callRotationMatrix.transferMatrixLocation(vehicle.getStateList());
                    stateList.addAll(vehicle.getStateList());
                }
                stateList.sort(new Comparator<LocationState>() {
                    @Override
                    public int compare(LocationState o1, LocationState o2) {
                        return o1.getCreateTime().compareTo(o2.getCreateTime());
                    }
                });
                String fileName = radar.getDeviceNum()+".txt";
                File file = new File(directory,fileName);
                FileOutputStream outputStream = new FileOutputStream(file,true);
                OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);
                bufferedWriter = new BufferedWriter(streamWriter);
                for(LocationState state: stateList){
                    String str = state.getVehicle().getVehicleId()+", "+state.getRotationX()+", "+state.getRotationY()+", "+state.getHeading()+", "+state.getTimestamp();
                    bufferedWriter.write(str);
                    bufferedWriter.newLine();
                }
            }finally {
                if(bufferedWriter!=null){
                    bufferedWriter.close();
                }
            }
        }
    }
}
