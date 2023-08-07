package com.ruoyi.traffic.service.area.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.traffic.domain.area.TrafficAreaEvaluationData;
import com.ruoyi.traffic.domain.area.TrafficAreaEvaluationHistory;

import com.ruoyi.traffic.mapper.area.TrafficAreaEvaluationDataMapper;
import com.ruoyi.traffic.service.area.ITrafficAreaEvaluationHistoryService;
import com.ruoyi.traffic.service.area.ITrafficAreaEvaluationDataService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.DataTruncation;
import java.util.List;

/**
 * @classname: rafficArea_Evaluation_Impl
 * @author: ouyanghua
 * @description: 区域评价的服务实现类
 * @date: 2023/7/24
 * @version: v1.0
 **/
@Service


public class TrafficAreaEvaluationDataDataImpl extends ServiceImpl<TrafficAreaEvaluationDataMapper, TrafficAreaEvaluationData> implements ITrafficAreaEvaluationDataService {



    @Autowired
    private ITrafficAreaEvaluationHistoryService trafficAreaEvaluationHistoryService;

    @Override
        public List<TrafficAreaEvaluationData> queryList(TrafficAreaEvaluationData trafficAreaEvaluationData) {
            LambdaQueryWrapper<TrafficAreaEvaluationData> queryWrapper = new LambdaQueryWrapper<>();
            // 区域id
            if (trafficAreaEvaluationData.getAreaId() != null) {
                queryWrapper.like(TrafficAreaEvaluationData::getAreaId, trafficAreaEvaluationData.getAreaId());
            }

            List<TrafficAreaEvaluationData> trafficAreaevaluationListData = baseMapper.selectList(queryWrapper);
            return trafficAreaevaluationListData;

    }

    @Transactional
    @Override
    public void addAreaEvaluationData(TrafficAreaEvaluationData trafficAreaEvaluationData) {
        baseMapper.insert(trafficAreaEvaluationData);
        // 同时新增一条历史记录
        // 添加年、月、日信息
        String day = DateUtils.getDay(trafficAreaEvaluationData.getCollectTime());
        String month = DateUtils.getMonth(trafficAreaEvaluationData.getCollectTime());
        String year = DateUtils.getYear(trafficAreaEvaluationData.getCollectTime());
        TrafficAreaEvaluationHistory trafficAreaEvaluationHistory = TrafficAreaEvaluationHistory.builder().areaId(trafficAreaEvaluationData.getAreaId())
                .evaluationTypeId(trafficAreaEvaluationData.getEvaluationTypeId())
                .value(trafficAreaEvaluationData.getValue())
                .collectTime(trafficAreaEvaluationData.getCollectTime())
                .Year(year)
                .Month(month)
                .Day(day).build();
        trafficAreaEvaluationHistoryService.addAreaEvaluationHistory(trafficAreaEvaluationHistory);

    }


    @Override
    public void deleteByIdList(List<Long> idList) {
        if (CollectionUtils.isNotEmpty(idList)) {
            baseMapper.deleteBatchIds(idList);
        }
    }

    @Override
    public TrafficAreaEvaluationData queryById(Long id) {
       TrafficAreaEvaluationData trafficAreaEvaluationData =baseMapper.selectById(id);
       return trafficAreaEvaluationData;
    }




}
