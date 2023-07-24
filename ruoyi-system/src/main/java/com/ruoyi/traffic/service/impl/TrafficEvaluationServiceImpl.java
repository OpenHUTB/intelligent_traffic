package com.ruoyi.traffic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.traffic.domain.TrafficEvaluation;
import com.ruoyi.traffic.mapper.TrafficEvaluationMapper;
import com.ruoyi.traffic.service.ITrafficEvaluationService;
import org.springframework.stereotype.Service;

/**
 * @classname: TrafficEvaluationServiceImpl
 * @author: ouyangdelong
 * @description: 路口评价指标服务实现类
 * @date: 2023/7/24
 * @version: v1.0
 **/
@Service
public class TrafficEvaluationServiceImpl extends ServiceImpl<TrafficEvaluationMapper, TrafficEvaluation>
        implements ITrafficEvaluationService {
}
