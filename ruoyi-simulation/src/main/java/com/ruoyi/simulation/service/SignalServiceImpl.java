package com.ruoyi.simulation.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.simulation.domain.TrafficLight;
import com.ruoyi.simulation.listener.ProcessCommandListener;
import com.ruoyi.simulation.listener.SignalControlListener;
import com.ruoyi.simulation.util.ResultUtil;
import com.ruoyi.simulation.util.TrafficLightUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 信控业务层实现类
 */
@Service
public class SignalServiceImpl implements SignalService {
    @Resource
    public RedisTemplate<String,Object> redisTemplate;
    @Resource
    private SignalControlListener signalControlListener;
    public ResultUtil<JSONObject> fixedRegulation(){
        ResultUtil<JSONObject> result = new ResultUtil<>();
        List<TrafficLight> trafficLightList = signalControlListener.trafficLightList;
        signalControlListener.fixedRegulation(trafficLightList);
        //临时存储信控方案
        SignalControlListener.setTemporarySignal(trafficLightList);
        TrafficLightUtil.setCarlaTrafficLight(redisTemplate, trafficLightList);
        JSONObject trafficData = SignalControlListener.getJunctionSignal(ProcessCommandListener.junctionId);
        result.setStatus(ResultUtil.Status.SUCCESS);
        result.setData(trafficData);
        return result;
    }

    @Override
    public ResultUtil<Map<Integer, JSONObject>> getSignalMap() {
        ResultUtil<Map<Integer, JSONObject>> result = new ResultUtil<>();
        Map<Integer, JSONObject> trafficDataMap = new HashMap<>();
        //获取指定路口的红绿灯时间
        Map<Integer,List<TrafficLight>> junctionLightMap = SignalControlListener.junctionLightMap;
        for(int junctionId: junctionLightMap.keySet()){
            JSONObject trafficData = SignalControlListener.getJunctionSignal(junctionId);
            trafficDataMap.put(junctionId, trafficData);
        }
        result.setData(trafficDataMap);
        result.setStatus(ResultUtil.Status.SUCCESS);
        return result;
    }

    @Override
    public ResultUtil<JSONObject> getSignalData(int junctionId) {
        ResultUtil<JSONObject> result = new ResultUtil<>();
        result.setStatus(ResultUtil.Status.SUCCESS);
        JSONObject trafficData = SignalControlListener.getJunctionSignal(junctionId);
        result.setData(trafficData);
        return result;
    }


}
