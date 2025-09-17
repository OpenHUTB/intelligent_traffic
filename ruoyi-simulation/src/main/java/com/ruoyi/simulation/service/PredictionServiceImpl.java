package com.ruoyi.simulation.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.simulation.call.CallUE4Engine;
import com.ruoyi.simulation.dao.PredictionMapper;
import com.ruoyi.simulation.domain.Prediction;
import com.ruoyi.simulation.util.Axis;
import com.ruoyi.simulation.util.ResultUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.transform.Result;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 预测信息业务层实现类
 */
@Service
public class PredictionServiceImpl implements PredictionService {
    @Resource
    private PredictionMapper predictionMapper;
    @Resource
    private CallUE4Engine callUE4Engine;
    @Override
    public ResultUtil<List<Axis>> getPredictionFlow(String model, String weatherCode) {
        ResultUtil<List<Axis>> result = new ResultUtil<>();
        if(StringUtils.equals(weatherCode,"1")){
            model = "weather_" + model;
        }
        List<Prediction> predictionList = this.predictionMapper.getNearestList();
        int length = predictionList.size();
        int[] flows = new int[length];
        int[] weatherCodes = new int[length];
        String[] times = new String[length];
        for(int i=0;i<length;i++){
            Prediction prediction = predictionList.get(i);
            flows[i] = prediction.getFlow();
            weatherCodes[i] = prediction.getWeatherCode();
            times[i] = prediction.getPredictionTime();
        }
        String command = "traffic_predict.py --model " + model
                + " --flow " + JSON.toJSONString(flows)
                + " --weather_code " + JSON.toJSONString(weatherCodes)
                + " --time_list "+JSON.toJSONString(times);
        String response = this.callUE4Engine.getInformation(command);
        List<Double> flowList = JSON.parseArray(response, Double.class);
        List<Axis> axisList = new ArrayList<>();
        for(int i=0;i<times.length;i++){
            axisList.add(new Axis(times[i],flowList.get(i)));
        }
        result.setStatus(ResultUtil.Status.SUCCESS);
        result.setData(axisList);
        return result;
    }
}
