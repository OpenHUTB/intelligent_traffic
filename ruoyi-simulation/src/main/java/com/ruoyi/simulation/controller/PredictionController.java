package com.ruoyi.simulation.controller;

import com.ruoyi.simulation.service.PredictionService;
import com.ruoyi.simulation.util.Axis;
import com.ruoyi.simulation.util.ResultUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 预测信息控制层
 */
@RestController
@RequestMapping("simulation/prediction")
public class PredictionController {
    @Resource
    private PredictionService predictionService;
    /**
     * 获取交通流量预测数据
     * @param model long_term_model-长期预测模型,short_term_model-短期预测模型,short_long_term_model-长短期预测模型
     * @param weatherCode 0-不融合天气特征,1-融合天气特征
     * @return
     */
    @GetMapping("predictionFlow")
    public ResultUtil<List<Axis>> getPredictionFlow(String model, String weatherCode){
        return this.predictionService.getPredictionFlow(model, weatherCode);
    }
}
