package com.ruoyi.traffic.service.intersection.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.traffic.domain.intersection.TrafficIntersectionEvaluationData;
import com.ruoyi.traffic.domain.intersection.TrafficIntersectionEvaluationHistory;
import com.ruoyi.traffic.mapper.intersection.TrafficIntersectionEvaluationDataMapper;
import com.ruoyi.traffic.service.intersection.ITrafficIntersectionEvaluationDataService;
import com.ruoyi.traffic.service.intersection.ITrafficIntersectionEvaluationHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @classname: TrafficIntersectionEvaluationDataServiceImpl
 * @author: ouyangdelong
 * @description: 路口评价指标实时数据实现类
 * @date: 2023/7/25
 * @version: v1.0
 **/
@Service
public class TrafficIntersectionEvaluationDataServiceImpl extends ServiceImpl<TrafficIntersectionEvaluationDataMapper, TrafficIntersectionEvaluationData>
        implements ITrafficIntersectionEvaluationDataService {

    @Autowired
    ITrafficIntersectionEvaluationHistoryService trafficIntersectionEvaluationHistoryService;
    @Override
    public List<TrafficIntersectionEvaluationData> queryList(TrafficIntersectionEvaluationData trafficEvaluationData) {
        LambdaQueryWrapper<TrafficIntersectionEvaluationData> queryWrapper = new LambdaQueryWrapper<>();
        //intersectionId
        if (StringUtils.isNotNull(trafficEvaluationData.getIntersectionId())) {
            queryWrapper.like(TrafficIntersectionEvaluationData::getIntersectionId, trafficEvaluationData.getIntersectionId());
        }
        //EvaluationId
        if (StringUtils.isNotNull(trafficEvaluationData.getEvaluationTypeId())) {
            queryWrapper.like(TrafficIntersectionEvaluationData::getEvaluationTypeId, trafficEvaluationData.getEvaluationTypeId());
        }
        return baseMapper.selectList(queryWrapper);
    }

    @Transactional
    @Override
    public void addEvaluationData(TrafficIntersectionEvaluationData trafficEvaluationData) {
        baseMapper.insert(trafficEvaluationData);
        // 同时新增一条历史记录
        // 添加年、月、日信息
        String day = DateUtils.getDay(trafficEvaluationData.getCollectTime());
        String month = DateUtils.getMonth(trafficEvaluationData.getCollectTime());
        String year = DateUtils.getYear(trafficEvaluationData.getCollectTime());
        TrafficIntersectionEvaluationHistory trafficIntersectionEvaluationHistory = TrafficIntersectionEvaluationHistory.builder().intersectionId(trafficEvaluationData.getIntersectionId())
                .evaluationTypeId(trafficEvaluationData.getEvaluationTypeId())
                .value(trafficEvaluationData.getValue())
                .collectTime(trafficEvaluationData.getCollectTime())
                .Year(year)
                .Month(month)
                .Day(day).build();
        trafficIntersectionEvaluationHistoryService.addEvaluationHistory(trafficIntersectionEvaluationHistory);
    }

    @Override
    public void updateEvaluationData(TrafficIntersectionEvaluationData trafficEvaluationData) {
        baseMapper.updateById(trafficEvaluationData);
    }

    @Override
    public void deleteEvaluationData(List<Long> idList) {
        baseMapper.deleteBatchIds(idList);
    }

    @Override
    public TrafficIntersectionEvaluationData queryById(Long id) {
        return baseMapper.selectById(id);
    }
}
