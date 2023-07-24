package com.ruoyi.traffic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ruoyi.common.utils.StringUtils;

import com.ruoyi.traffic.domain.TrafficArea_Evaluation;
import com.ruoyi.traffic.domain.TrafficArea_Evaluation_History;

import com.ruoyi.traffic.mapper.TrafficArea_Evaluation_Mapper;
import com.ruoyi.traffic.service.ITrafficArea_Evaluation_Service;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrafficArea_Evaluation_Impl extends ServiceImpl<TrafficArea_Evaluation_Mapper,TrafficArea_Evaluation> implements ITrafficArea_Evaluation_Service {
    @Override

        public List<TrafficArea_Evaluation> queryList(TrafficArea_Evaluation trafficArea_evaluation) {
            LambdaQueryWrapper<TrafficArea_Evaluation> queryWrapper = new LambdaQueryWrapper<>();
            // name
            if (StringUtils.isNotBlank(trafficArea_evaluation.getName())) {
                queryWrapper.like(TrafficArea_Evaluation::getName, trafficArea_evaluation.getName());
            }

            List<TrafficArea_Evaluation> trafficArea_evaluationList = baseMapper.selectList(queryWrapper);
            return trafficArea_evaluationList;

    }

    @Override
    public void addAreaEvaluation(TrafficArea_Evaluation trafficArea_Evaluation) {
        baseMapper.insert(trafficArea_Evaluation);
    }

    @Autowired

    private  ITrafficArea_Evaluation_History_Impl  iTrafficArea_Evaluation_History_Impl;
    @Override
    public void updateAreaEvaluation(TrafficArea_Evaluation trafficArea_Evaluation) {

        TrafficArea_Evaluation_History history = new TrafficArea_Evaluation_History();

        history.setIntersectionId(trafficArea_Evaluation.getIntersectionId());
        history.setName(trafficArea_Evaluation.getName());
        history.setAverageSpeed(trafficArea_Evaluation.getAverageSpeed());
        history.setAverageDelay(trafficArea_Evaluation.getAverageDelay());
        history.setCongestionIndex(trafficArea_Evaluation.getCongestionIndex());
        history.setYear(trafficArea_Evaluation.getYear());
        history.setMonth(trafficArea_Evaluation.getMonth());
        history.setDay(trafficArea_Evaluation.getDay());
//         更新实时表的同时往历史表中插入一条相同的记录
        iTrafficArea_Evaluation_History_Impl.addAreaEvaluationHistory(history);

        baseMapper.updateById(trafficArea_Evaluation);
    }

    @Override
    public void deleteByIdList(List<Long> idList) {
        if (CollectionUtils.isNotEmpty(idList)) {
            baseMapper.deleteBatchIds(idList);
        }
    }

    @Override
    public TrafficArea_Evaluation queryById(Long id) {
       TrafficArea_Evaluation trafficAreaEvaluation=baseMapper.selectById(id);
       return trafficAreaEvaluation;
    }




}
