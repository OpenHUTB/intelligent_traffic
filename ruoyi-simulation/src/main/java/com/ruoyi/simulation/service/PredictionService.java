package com.ruoyi.simulation.service;

import com.ruoyi.simulation.util.Axis;
import com.ruoyi.simulation.util.ResultUtil;

import java.util.List;

/**
 * 预测信息业务层
 */
public interface PredictionService {
    /**
     * 获取交通流量预测数据
     * @param model Long-term_model-长期预测模型,Short-term_model-短期预测模型,short_long_term_model-长短期预测模型
     * @param weatherCode 0-不融合天气特征,1-融合天气特征
     * @return
     */
    public ResultUtil<List<Axis>> getPredictionFlow(String model, String weatherCode);
}
