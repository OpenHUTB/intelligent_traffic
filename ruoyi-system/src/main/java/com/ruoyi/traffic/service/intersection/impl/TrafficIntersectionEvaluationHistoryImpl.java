package com.ruoyi.traffic.service.intersection.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.traffic.domain.evaluationType.TrafficEvaluationType;
import com.ruoyi.traffic.domain.intersection.TrafficIntersection;
import com.ruoyi.traffic.domain.intersection.TrafficIntersectionEvaluationData;
import com.ruoyi.traffic.domain.intersection.TrafficIntersectionEvaluationHistory;
import com.ruoyi.traffic.mapper.intersection.TrafficIntersectionEvaluationHistoryMapper;
import com.ruoyi.traffic.service.intersection.ITrafficIntersectionEvaluationHistoryService;
import com.ruoyi.traffic.vo.TrafficIntersectionEvaluationDataVo;
import com.ruoyi.traffic.vo.TrafficIntersectionEvaluationHistoryVo;
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
    public TrafficIntersectionEvaluationHistoryVo queryById(Long id) {
        MPJLambdaWrapper<TrafficIntersectionEvaluationHistory> queryWrapper = new MPJLambdaWrapper<>();
        queryWrapper.like(TrafficIntersectionEvaluationHistory::getId, id);
        TrafficIntersectionEvaluationHistoryVo vo = baseMapper.selectJoinOne(TrafficIntersectionEvaluationHistoryVo.class,
                queryWrapper
                        .select(TrafficIntersection::getName, TrafficIntersection::getLongitude, TrafficIntersection::getLatitude)
                        .select(TrafficEvaluationType::getName, TrafficEvaluationType::getType)
                        .select(TrafficIntersectionEvaluationHistory::getYear, TrafficIntersectionEvaluationHistory::getMonth,
                                TrafficIntersectionEvaluationHistory::getDay, TrafficIntersectionEvaluationHistory::getValue, TrafficIntersectionEvaluationHistory::getCollectTime)
                        .selectAs(TrafficIntersection::getName, TrafficIntersectionEvaluationHistoryVo::getIntersectionName)
                        .selectAs(TrafficEvaluationType::getName, TrafficIntersectionEvaluationHistoryVo::getEvaluationName)
                        .leftJoin(TrafficIntersection.class, TrafficIntersection::getId, TrafficIntersectionEvaluationHistory::getIntersectionId)
                        .leftJoin(TrafficEvaluationType.class, TrafficEvaluationType::getId, TrafficIntersectionEvaluationHistory::getEvaluationTypeId)
        );
        return vo;
    }

    @Override
    public List<TrafficIntersectionEvaluationHistoryVo> relatedQueryList(TrafficIntersectionEvaluationHistory trafficIntersectionEvaluationHistory) {
        MPJLambdaWrapper<TrafficIntersectionEvaluationHistory> queryWrapper = new MPJLambdaWrapper<>();
        // 路口id
        if (StringUtils.isNotNull(trafficIntersectionEvaluationHistory.getIntersectionId())) {
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
        queryWrapper.orderByDesc(TrafficIntersectionEvaluationHistory::getCollectTime);
        List<TrafficIntersectionEvaluationHistoryVo> vo = baseMapper.selectJoinList(TrafficIntersectionEvaluationHistoryVo.class,
                queryWrapper
                        .select(TrafficIntersection::getName, TrafficIntersection::getLongitude, TrafficIntersection::getLatitude)
                        .select(TrafficEvaluationType::getName, TrafficEvaluationType::getType)
                        .select(TrafficIntersectionEvaluationHistory::getYear, TrafficIntersectionEvaluationHistory::getMonth,
                                TrafficIntersectionEvaluationHistory::getDay, TrafficIntersectionEvaluationHistory::getValue, TrafficIntersectionEvaluationHistory::getCollectTime)
                        .selectAs(TrafficIntersection::getName, TrafficIntersectionEvaluationHistoryVo::getIntersectionName)
                        .selectAs(TrafficEvaluationType::getName, TrafficIntersectionEvaluationHistoryVo::getEvaluationName)
                        .leftJoin(TrafficIntersection.class, TrafficIntersection::getId, TrafficIntersectionEvaluationHistory::getIntersectionId)
                        .leftJoin(TrafficEvaluationType.class, TrafficEvaluationType::getId, TrafficIntersectionEvaluationHistory::getEvaluationTypeId)
        );
        return vo;

    }


}
