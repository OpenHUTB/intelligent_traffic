package com.ruoyi.traffic.service.intersection.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.traffic.domain.intersection.TrafficIntersectionEvaluationData;
import com.ruoyi.traffic.mapper.intersection.TrafficIntersectionEvaluationDataMapper;
import com.ruoyi.traffic.service.intersection.ITrafficIntersectionEvaluationDataService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @classname: TrafficEvaluationDataServiceImpl
 * @author: ouyangdelong
 * @description: 路口评价指标实时数据实现类
 * @date: 2023/7/25
 * @version: v1.0
 **/
@Service
public class ITrafficIntersectionEvaluationDataServiceImpl extends ServiceImpl<TrafficIntersectionEvaluationDataMapper, TrafficIntersectionEvaluationData>
        implements ITrafficIntersectionEvaluationDataService {
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
        List<TrafficIntersectionEvaluationData> trafficEvaluationDataList = baseMapper.selectList(queryWrapper);
        return trafficEvaluationDataList;
    }

    @Override
    public void addEvaluationData(TrafficIntersectionEvaluationData trafficEvaluationData) {
        baseMapper.insert(trafficEvaluationData);
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
        TrafficIntersectionEvaluationData trafficEvaluationData = baseMapper.selectById(id);
        return trafficEvaluationData;
    }
}
