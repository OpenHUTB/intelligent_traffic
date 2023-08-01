package com.ruoyi.traffic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.traffic.domain.TrafficEvaluationData;
import com.ruoyi.traffic.mapper.TrafficEvaluationDataMapper;
import com.ruoyi.traffic.service.ITrafficEvaluationDataService;
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
public class ITrafficEvaluationDataServiceImpl extends ServiceImpl<TrafficEvaluationDataMapper, TrafficEvaluationData>
        implements ITrafficEvaluationDataService {
    @Override
    public List<TrafficEvaluationData> queryList(TrafficEvaluationData trafficEvaluationData) {
        LambdaQueryWrapper<TrafficEvaluationData> queryWrapper = new LambdaQueryWrapper<>();
        //intersectionId
        if (StringUtils.isNotNull(trafficEvaluationData.getIntersectionId())) {
            queryWrapper.like(TrafficEvaluationData::getIntersectionId, trafficEvaluationData.getIntersectionId());
        }
        //EvaluationId
        if (StringUtils.isNotNull(trafficEvaluationData.getEvaluationTypeId())) {
            queryWrapper.like(TrafficEvaluationData::getEvaluationTypeId, trafficEvaluationData.getEvaluationTypeId());
        }
        List<TrafficEvaluationData> trafficEvaluationDataList = baseMapper.selectList(queryWrapper);
        return trafficEvaluationDataList;
    }

    @Override
    public void addEvaluationData(TrafficEvaluationData trafficEvaluationData) {
        baseMapper.insert(trafficEvaluationData);
    }

    @Override
    public void updateEvaluationData(TrafficEvaluationData trafficEvaluationData) {
        baseMapper.updateById(trafficEvaluationData);
    }

    @Override
    public void deleteEvaluationData(List<Long> idList) {
        baseMapper.deleteBatchIds(idList);
    }

    @Override
    public TrafficEvaluationData queryById(Long id) {
        TrafficEvaluationData trafficEvaluationData = baseMapper.selectById(id);
        return trafficEvaluationData;
    }
}
