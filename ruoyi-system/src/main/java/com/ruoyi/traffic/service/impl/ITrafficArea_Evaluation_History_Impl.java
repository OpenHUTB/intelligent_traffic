package com.ruoyi.traffic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.StringUtils;

import com.ruoyi.traffic.domain.TrafficArea_Evaluation_History;

import com.ruoyi.traffic.mapper.TrafficArea_Evaluation_History_Mapper;
import com.ruoyi.traffic.service.ITrafficArea_Evaluation_History_Service;
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
public class ITrafficArea_Evaluation_History_Impl extends ServiceImpl<TrafficArea_Evaluation_History_Mapper, TrafficArea_Evaluation_History> implements ITrafficArea_Evaluation_History_Service {
    @Override
    public List<TrafficArea_Evaluation_History> queryList(TrafficArea_Evaluation_History trafficArea_evaluation_history) {
        LambdaQueryWrapper<TrafficArea_Evaluation_History> queryWrapper = new LambdaQueryWrapper<>();
        // name
        if (StringUtils.isNotBlank(trafficArea_evaluation_history.getName())) {
            queryWrapper.like(TrafficArea_Evaluation_History::getName, trafficArea_evaluation_history.getName());
        }
        // 年份
        if (StringUtils.isNotBlank(trafficArea_evaluation_history.getYear())) {
            queryWrapper.eq(TrafficArea_Evaluation_History::getYear, trafficArea_evaluation_history.getYear());
        }
        // 月份
        if (StringUtils.isNotBlank(trafficArea_evaluation_history.getMonth())) {
            queryWrapper.eq(TrafficArea_Evaluation_History::getMonth, trafficArea_evaluation_history.getMonth());
        }
        //日期
        if (StringUtils.isNotBlank(trafficArea_evaluation_history.getDay())) {
            queryWrapper.eq(TrafficArea_Evaluation_History::getDay, trafficArea_evaluation_history.getDay());
        }

        List<TrafficArea_Evaluation_History> trafficArea_evaluation_HistoryList = baseMapper.selectList(queryWrapper);
        return trafficArea_evaluation_HistoryList;

    }

    @Override
    public void addAreaEvaluationHistory(TrafficArea_Evaluation_History trafficArea_evaluation_history) {
        baseMapper.insert(trafficArea_evaluation_history);
    }

    @Override
    public void updateAreaEvaluationHistory(TrafficArea_Evaluation_History trafficArea_evaluation_history) {
        baseMapper.updateById(trafficArea_evaluation_history);
    }

    @Override
    public void deleteByIdList(List<Long> idList) {
        if (CollectionUtils.isNotEmpty(idList)) {
            baseMapper.deleteBatchIds(idList);
        }
    }

    @Override
    public TrafficArea_Evaluation_History queryById(Long id) {
        TrafficArea_Evaluation_History trafficArea_evaluation_history = baseMapper.selectById(id);
        return trafficArea_evaluation_history;
    }



}
