package com.ruoyi.simulation.service;

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
import java.util.List;

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
        ResultUtil<JSONObject> result = new ResultUtil<JSONObject>();
        List<TrafficLight> trafficLightList = new ArrayList<TrafficLight>();
        for(int junctionId: SignalControlListener.junctionLightMap.keySet()){
            trafficLightList.addAll(SignalControlListener.junctionLightMap.get(junctionId));
        }
        signalControlListener.fixedRegulation(trafficLightList);
        //临时存储信控方案
        SignalControlListener.setTemporarySignal(trafficLightList);
        TrafficLightUtil.setCarlaTrafficLight(redisTemplate, trafficLightList);
        JSONObject trafficData = SignalControlListener.getSignalControl(ProcessCommandListener.junctionId);
        result.setStatus(ResultUtil.Status.SUCCESS);
        result.setData(trafficData);
        return result;
    }
}
