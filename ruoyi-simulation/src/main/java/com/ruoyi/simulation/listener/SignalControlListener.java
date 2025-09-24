package com.ruoyi.simulation.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.simulation.call.CallUE4Engine;
import com.ruoyi.simulation.dao.*;
import com.ruoyi.simulation.domain.*;
import com.ruoyi.simulation.domain.Signalbase.TrafficLightState;
import com.ruoyi.simulation.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

/**
 * 原始信控数据管理的监听器
 */
@Component
public class SignalControlListener implements ServletContextListener {
    private static Logger logger = LoggerFactory.getLogger(SignalControlListener.class);
    /**
     * 每个路口id对应的红绿灯集合
     */
    public static final Map<Integer,List<TrafficLight>> junctionLightMap = new HashMap<>();
    public List<TrafficLight> trafficLightList;
    @Resource
    private SignalBaselMapper signalbaselMapper;
    @Resource
    private CallUE4Engine callUE4Engine;
    @Resource
    private TrafficLightMapper trafficLightMapper;
    @Resource
    private DurationMapper phaseMapper;
    @Resource
    private FlowRecordMapper recordMapper;
    @Resource
    private GreenWaveMapper greenWaveMapper;
    @Resource
    public RedisTemplate<String,Object> redisTemplate;
    @Override
    public void contextInitialized(ServletContextEvent event) {
        this.trafficLightList = initialTrafficLight();
        TrafficLightUtil.loggerSignal(trafficLightList,"优化前信控方案");
        this.fixedRegulation(this.trafficLightList);
        TrafficLightUtil.loggerSignal(trafficLightList,"绿波信控方案");
        //初始化一个周期内每秒钟对应的红绿灯状态
        TrafficLightUtil.initialStateArr(trafficLightList);
        //临时存储信控方setTemporarySignal案
        setTemporarySignal(this.trafficLightList);
        TrafficLightUtil.setCarlaTrafficLight(this.redisTemplate, this.trafficLightList);
        //this.callUE4Engine.executeExample("traffic_light_settings.py");
    }

    /**
     * 初始化红绿灯数据
     * @return
     */
    public List<TrafficLight> initialTrafficLight(){
        //获取不同交通灯对应的信控信息
        List<Signalbase> signalbaseList = this.signalbaselMapper.getSignalBaseList();
        List<TrafficLight> trafficLightList = this.trafficLightMapper.selectList(new QueryWrapper<>());
        //获取红绿灯id与红绿灯的映射关系
        Map<Integer, TrafficLight> trafficLightMap = TrafficLightUtil.getTrafficLightMap(trafficLightList);
        Map<Integer, TrafficLight> temporaryMap = new HashMap<>();
        for(Signalbase signal: signalbaseList){
            //设置不同红绿灯对应的信控数据
            int trafficLightId = signal.getTrafficLightId();
            TrafficLight trafficLight = trafficLightMap.get(trafficLightId);
            if(signal.getLightState()== TrafficLightState.RED){
                if(trafficLight.getRedTime()==null){
                    trafficLight.setRedTime(0);
                }
                trafficLight.setRedTime(trafficLight.getRedTime()+signal.getDuration());
                trafficLight.addState(TrafficLightState.RED, signal.getDuration());
            }else if(signal.getLightState()== TrafficLightState.GREEN || signal.getLightState()== TrafficLightState.GREEN_YELLOW){
                //获取绿灯所在相位
                if(trafficLight.getGreenPhase()==null){
                    int phaseId = signal.getPhase();
                    trafficLight.setGreenPhase(phaseId);
                }
                trafficLight.setGreenTime(trafficLight.getGreenTime()+signal.getDuration());
                trafficLight.addState(TrafficLightState.GREEN, signal.getDuration());
            }else if(signal.getLightState()== TrafficLightState.YELLOW){
                if(trafficLight.getYellowTime()==null){
                    trafficLight.setYellowTime(0);
                }
                trafficLight.setYellowTime(trafficLight.getYellowTime()+signal.getDuration());
                trafficLight.addState(TrafficLightState.YELLOW, signal.getDuration());
            }
            temporaryMap.putIfAbsent(trafficLightId,trafficLight);
        }
        trafficLightList = new ArrayList<>(temporaryMap.values());
        for(TrafficLight trafficLight: trafficLightList){
            List<StateStage> stageList = TrafficLightUtil.mergeStage(trafficLight.getStageList());
            trafficLight.setStageList(stageList);
        }
        return trafficLightList;
    }
    /**
     * 固定周期信控优化
     * @param trafficLightList
     */
    public void fixedRegulation(List<TrafficLight> trafficLightList) {
        //获取区域中的红绿灯时段划分信息列表
        List<Duration> phaseList = this.phaseMapper.selectList(new QueryWrapper<>());
        int durationId = this.getDurationId(phaseList);
        //获取指定时段下各个交通灯的平均流量记录
        LambdaQueryWrapper<FlowRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FlowRecord::getDurationId, durationId);
        List<FlowRecord> recordList = this.recordMapper.selectList(queryWrapper);
        //获取所有的绿波组
        List<GreenWave> waveList = this.greenWaveMapper.selectList(new QueryWrapper<>());
        FixedRegulation.assignSignalControl(trafficLightList, recordList, waveList);
    }
    private int getDurationId(List<Duration> phaseList){
        return 2;
    }
    /**
     * 自适应信控优化
     * @throws InterruptedException
     */
    public void automaticRegulation() {
        //获取不同路口对应的红绿灯集合
        List<TrafficLight> trafficLightList = this.trafficLightMapper.selectList(new QueryWrapper<>());
        new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    try {
                        Thread.sleep(3 * 60000);
                        AutoRegulation.assignSignalControl(trafficLightList);
                        //临时存储信控方案
                        setTemporarySignal(trafficLightList);
                        TrafficLightUtil.setCarlaTrafficLight(redisTemplate, trafficLightList);
                        Thread.sleep(27 * 60000);
                    } catch (InterruptedException e) {
                        LoggerUtil.printLoggerStace(e);
                    }
                } while(true);
            }
        }).start();
    }
    /**
     * 临时存储信控方案信息
     * @param trafficLightList
     */
    public static void setTemporarySignal(List<TrafficLight> trafficLightList){
        for(TrafficLight trafficLight: trafficLightList){
            //将红绿灯按路口进行划分
            int junctionId = trafficLight.getJunctionId();
            junctionLightMap.putIfAbsent(junctionId, new ArrayList<>());
            List<TrafficLight> tempList = junctionLightMap.get(junctionId);
            int index = ListUtil.getIndex(tempList, trafficLight);
            if(index!=-1){
                tempList.set(index, trafficLight);
            }else{
                tempList.add(trafficLight);
            }
        }
        for(int junctionId: junctionLightMap.keySet()){
            logger.info("============================================================"+junctionId+"============================================================");
            trafficLightList = junctionLightMap.get(junctionId);
            for(TrafficLight trafficLight:trafficLightList){
                logger.info(JSON.toJSONString(trafficLight.getStageList()));
            }
        }
    }
    /**
     * 获取指定路口的红绿灯时间
     * @param junctionId
     * @return
     */
    public static JSONObject getJunctionSignal(int junctionId){
        List<TrafficLight> trafficLightList = SignalControlListener.junctionLightMap.get(junctionId);
        if(trafficLightList==null){
            return null;
        }
        JSONObject trafficData = new JSONObject();
        for(TrafficLight trafficLight: trafficLightList){
            String fromDirection = trafficLight.getFromDirection().toLowerCase();
            trafficData.putIfAbsent(fromDirection, new JSONObject());
            JSONObject directionMap = trafficData.getJSONObject(fromDirection);
            String turnDirection = trafficLight.getTurnDirection().toLowerCase();
            directionMap.put(turnDirection, new JSONObject());
            JSONObject signalMap = directionMap.getJSONObject(turnDirection);
            signalMap.put("redDurationTime", trafficLight.getRedTime());
            signalMap.put("greenDurationTime", trafficLight.getGreenTime());
            signalMap.put("yellowDurationTime", trafficLight.getYellowTime());
        }
        return trafficData;
    }
}
