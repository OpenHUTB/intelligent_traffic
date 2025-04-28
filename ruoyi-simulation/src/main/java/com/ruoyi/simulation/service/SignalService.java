package com.ruoyi.simulation.service;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.simulation.util.ResultUtil;

/**
 * 信控业务层接口
 */
public interface SignalService {
    /**
     * 固定信控调优
     * @return
     */
    public ResultUtil<JSONObject> fixedRegulation();
}
