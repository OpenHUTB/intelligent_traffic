package com.ruoyi.traffic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ruoyi.common.utils.StringUtils;

import com.ruoyi.traffic.domain.TrafficAreaEvaluation;
import com.ruoyi.traffic.domain.TrafficAreaEvaluationHistory;

import com.ruoyi.traffic.mapper.TrafficAreaEvaluationMapper;
import com.ruoyi.traffic.service.ITrafficAreaEvaluationHistoryService;
import com.ruoyi.traffic.service.ITrafficAreaEvaluationService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @classname: rafficArea_Evaluation_Impl
 * @author: ouyanghua
 * @description: 区域评价的服务实现类
 * @date: 2023/7/24
 * @version: v1.0
 **/
@Service


public class TrafficAreaEvaluationImpl extends ServiceImpl<TrafficAreaEvaluationMapper, TrafficAreaEvaluation> implements ITrafficAreaEvaluationService {



    @Autowired
    private ITrafficAreaEvaluationHistoryService iTrafficAreaEvaluationHistoryService;

    @Override
        public List<TrafficAreaEvaluation> queryList(TrafficAreaEvaluation trafficArea_evaluation) {
            LambdaQueryWrapper<TrafficAreaEvaluation> queryWrapper = new LambdaQueryWrapper<>();
            // name
            if (StringUtils.isNotBlank(trafficArea_evaluation.getName())) {
                queryWrapper.like(TrafficAreaEvaluation::getName, trafficArea_evaluation.getName());
            }

            List<TrafficAreaEvaluation> trafficAreaevaluationList = baseMapper.selectList(queryWrapper);
            return trafficAreaevaluationList;

    }

    @Override
    public void addAreaEvaluation(TrafficAreaEvaluation trafficArea_Evaluation) {
        baseMapper.insert(trafficArea_Evaluation);
    }



    @Override
    public void updateAreaEvaluation(TrafficAreaEvaluation trafficArea_Evaluation) {

        TrafficAreaEvaluationHistory history = new TrafficAreaEvaluationHistory();

        history.setIntersectionId(trafficArea_Evaluation.getIntersectionId());
        history.setName(trafficArea_Evaluation.getName());
        history.setAverageSpeed(trafficArea_Evaluation.getAverageSpeed());
        history.setAverageDelay(trafficArea_Evaluation.getAverageDelay());
        history.setCongestionIndex(trafficArea_Evaluation.getCongestionIndex());
        history.setYear(trafficArea_Evaluation.getYear());
        history.setMonth(trafficArea_Evaluation.getMonth());
        history.setDay(trafficArea_Evaluation.getDay());
//         更新实时表的同时往历史表中插入一条相同的记录
        iTrafficAreaEvaluationHistoryService.addAreaEvaluationHistory(history);

        baseMapper.updateById(trafficArea_Evaluation);
    }

    @Override
    public void deleteByIdList(List<Long> idList) {
        if (CollectionUtils.isNotEmpty(idList)) {
            baseMapper.deleteBatchIds(idList);
        }
    }

    @Override
    public TrafficAreaEvaluation queryById(Long id) {
       TrafficAreaEvaluation trafficAreaEvaluation=baseMapper.selectById(id);
       return trafficAreaEvaluation;
    }




}
