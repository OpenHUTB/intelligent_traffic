package com.ruoyi.simulation.service;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.simulation.util.ResultUtil;

import java.util.List;
import java.util.Map;

/**
 * 信控业务层接口
 */
public interface SignalService {
    /**
     * 固定信控调优
     * @return
     */
    public ResultUtil<JSONObject> fixedRegulation();

    /**
     * 查询所有信控数据
     * @return
     */
    public ResultUtil<Map<Integer, JSONObject>> getSignalMap();

    /**
     * 根据路口id获取信控数据
     * @param junctionId
     * @return
     */
    public ResultUtil<JSONObject> getSignalData(int junctionId);
}
