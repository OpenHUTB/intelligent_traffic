package com.ruoyi.simulation.controller;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.simulation.service.SignalService;
import com.ruoyi.simulation.util.ResultUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 信控数据库访问层
 */
@RestController
@RequestMapping("simulation/signal")
public class SignalController {
    @Resource
    private SignalService signalService;
    /**
     * 固定信控调优
     * @return
     */
    @PostMapping("fixedRegulation")
    public ResultUtil<JSONObject> fixedRegulation(){
        return this.signalService.fixedRegulation();
    }
    /**
     * 查询所有信控数据
     * @return
     */
    @GetMapping("signalMap")
    public ResultUtil<Map<Integer, JSONObject>> getSignalMap(){
        return this.signalService.getSignalMap();
    }
    /**
     * 根据路口id获取信控数据
     * @param junctionId
     * @return
     */
    @GetMapping("signalData")
    public ResultUtil<JSONObject> getSignalData(int junctionId){
        return this.signalService.getSignalData(junctionId);
    }
}
