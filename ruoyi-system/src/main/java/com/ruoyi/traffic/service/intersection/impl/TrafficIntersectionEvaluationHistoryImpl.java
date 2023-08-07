package com.ruoyi.traffic.service.intersection.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.traffic.domain.intersection.TrafficIntersectionEvaluationHistory;
import com.ruoyi.traffic.mapper.intersection.TrafficIntersectionEvaluationHistoryMapper;
import com.ruoyi.traffic.service.intersection.ITrafficIntersectionEvaluationHistoryService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @classname: TrafficIntersectionEvaluationHistoryImpl
 * @author: ouyangdelong
 * @description: 历史路口评价的服务实现类
 * @date: 2023/8/7
 * @version: v1.0
 **/
@Service
public class TrafficIntersectionEvaluationHistoryImpl extends ServiceImpl<TrafficIntersectionEvaluationHistoryMapper, TrafficIntersectionEvaluationHistory>
        implements ITrafficIntersectionEvaluationHistoryService {
    @Override
    public List<TrafficIntersectionEvaluationHistory> queryList(TrafficIntersectionEvaluationHistory trafficIntersectionEvaluationHistory) {
        LambdaQueryWrapper<TrafficIntersectionEvaluationHistory> queryWrapper = new LambdaQueryWrapper<>();
        // 路口id
        if (trafficIntersectionEvaluationHistory.getIntersectionId() != null) {
            queryWrapper.like(TrafficIntersectionEvaluationHistory::getIntersectionId, trafficIntersectionEvaluationHistory.getIntersectionId());
        }
        // 年份
        if (StringUtils.isNotBlank(trafficIntersectionEvaluationHistory.getYear())) {
            queryWrapper.eq(TrafficIntersectionEvaluationHistory::getYear, trafficIntersectionEvaluationHistory.getYear());
        }
        // 月份
        if (StringUtils.isNotBlank(trafficIntersectionEvaluationHistory.getMonth())) {
            queryWrapper.eq(TrafficIntersectionEvaluationHistory::getMonth, trafficIntersectionEvaluationHistory.getMonth());
        }
        //日期
        if (StringUtils.isNotBlank(trafficIntersectionEvaluationHistory.getDay())) {
            queryWrapper.eq(TrafficIntersectionEvaluationHistory::getDay, trafficIntersectionEvaluationHistory.getDay());
        }

        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public void addEvaluationHistory(TrafficIntersectionEvaluationHistory trafficIntersectionEvaluationHistory) {
        baseMapper.insert(trafficIntersectionEvaluationHistory);
    }

    @Override
    public void updateEvaluationHistory(TrafficIntersectionEvaluationHistory trafficIntersectionEvaluationHistory) {
        baseMapper.updateById(trafficIntersectionEvaluationHistory);
    }

    @Override
    public void deleteEvaluationHistory(List<Long> idList) {
        if (CollectionUtils.isNotEmpty(idList)) {
            baseMapper.deleteBatchIds(idList);
        }
    }

    @Override
    public TrafficIntersectionEvaluationHistory queryById(Long id) {
        return baseMapper.selectById(id);
    }


}
