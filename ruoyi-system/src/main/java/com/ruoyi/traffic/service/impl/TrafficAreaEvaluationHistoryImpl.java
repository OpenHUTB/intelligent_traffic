package com.ruoyi.traffic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.StringUtils;

import com.ruoyi.traffic.domain.TrafficAreaEvaluationHistory;

import com.ruoyi.traffic.mapper.TrafficAreaEvaluationHistoryMapper;
import com.ruoyi.traffic.service.ITrafficAreaEvaluationHistoryService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @classname: ITrafficArea_Evaluation_History_Impl
 * @author: ouyanghua
 * @description: 历史区域评价的服务实现类
 * @date: 2023/7/24
 * @version: v1.0
 **/
@Service
public class TrafficAreaEvaluationHistoryImpl extends ServiceImpl<TrafficAreaEvaluationHistoryMapper, TrafficAreaEvaluationHistory> implements ITrafficAreaEvaluationHistoryService {
    @Override
    public List<TrafficAreaEvaluationHistory> queryList(TrafficAreaEvaluationHistory trafficArea_evaluation_history) {
        LambdaQueryWrapper<TrafficAreaEvaluationHistory> queryWrapper = new LambdaQueryWrapper<>();
        // name
        if (StringUtils.isNotBlank(trafficArea_evaluation_history.getName())) {
            queryWrapper.like(TrafficAreaEvaluationHistory::getName, trafficArea_evaluation_history.getName());
        }
        // 年份
        if (StringUtils.isNotBlank(trafficArea_evaluation_history.getYear())) {
            queryWrapper.eq(TrafficAreaEvaluationHistory::getYear, trafficArea_evaluation_history.getYear());
        }
        // 月份
        if (StringUtils.isNotBlank(trafficArea_evaluation_history.getMonth())) {
            queryWrapper.eq(TrafficAreaEvaluationHistory::getMonth, trafficArea_evaluation_history.getMonth());
        }
        //日期
        if (StringUtils.isNotBlank(trafficArea_evaluation_history.getDay())) {
            queryWrapper.eq(TrafficAreaEvaluationHistory::getDay, trafficArea_evaluation_history.getDay());
        }

        List<TrafficAreaEvaluationHistory> trafficArea_evaluation_HistoryList = baseMapper.selectList(queryWrapper);
        return trafficArea_evaluation_HistoryList;

    }

    @Override
    public void addAreaEvaluationHistory(TrafficAreaEvaluationHistory trafficArea_evaluation_history) {
        baseMapper.insert(trafficArea_evaluation_history);
    }

    @Override
    public void updateAreaEvaluationHistory(TrafficAreaEvaluationHistory trafficArea_evaluation_history) {
        baseMapper.updateById(trafficArea_evaluation_history);
    }

    @Override
    public void deleteByIdList(List<Long> idList) {
        if (CollectionUtils.isNotEmpty(idList)) {
            baseMapper.deleteBatchIds(idList);
        }
    }

    @Override
    public TrafficAreaEvaluationHistory queryById(Long id) {
        TrafficAreaEvaluationHistory trafficArea_evaluation_history = baseMapper.selectById(id);
        return trafficArea_evaluation_history;
    }



}
