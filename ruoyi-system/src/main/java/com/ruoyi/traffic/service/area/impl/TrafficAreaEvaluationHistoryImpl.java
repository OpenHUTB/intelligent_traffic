package com.ruoyi.traffic.service.area.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.ruoyi.common.utils.StringUtils;

import com.ruoyi.traffic.domain.area.TrafficArea;
import com.ruoyi.traffic.domain.area.TrafficAreaEvaluationData;
import com.ruoyi.traffic.domain.area.TrafficAreaEvaluationHistory;

import com.ruoyi.traffic.domain.evaluationType.TrafficEvaluationType;
import com.ruoyi.traffic.dto.AreaEvaluationRankDTO;
import com.ruoyi.traffic.mapper.area.TrafficAreaEvaluationHistoryMapper;
import com.ruoyi.traffic.service.area.ITrafficAreaEvaluationHistoryService;
import com.ruoyi.traffic.vo.TrafficAreaEvaluationDataRankVO;
import com.ruoyi.traffic.vo.TrafficAreaEvaluationHistoryRankVo;
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
    public List<TrafficAreaEvaluationHistory> queryList(TrafficAreaEvaluationHistory trafficAreaEvaluationHistory) {
        LambdaQueryWrapper<TrafficAreaEvaluationHistory> queryWrapper = new LambdaQueryWrapper<>();
        // 区域id
        if (trafficAreaEvaluationHistory.getAreaId() != null) {
            queryWrapper.like(TrafficAreaEvaluationHistory::getAreaId, trafficAreaEvaluationHistory.getAreaId());
        }
        // 年份
        if (StringUtils.isNotBlank(trafficAreaEvaluationHistory.getYear())) {
            queryWrapper.eq(TrafficAreaEvaluationHistory::getYear, trafficAreaEvaluationHistory.getYear());
        }
        // 月份
        if (StringUtils.isNotBlank(trafficAreaEvaluationHistory.getMonth())) {
            queryWrapper.eq(TrafficAreaEvaluationHistory::getMonth, trafficAreaEvaluationHistory.getMonth());
        }
        //日期
        if (StringUtils.isNotBlank(trafficAreaEvaluationHistory.getDay())) {
            queryWrapper.eq(TrafficAreaEvaluationHistory::getDay, trafficAreaEvaluationHistory.getDay());
        }

        List<TrafficAreaEvaluationHistory> trafficArea_evaluation_HistoryList = baseMapper.selectList(queryWrapper);
        return trafficArea_evaluation_HistoryList;

    }

    @Override
    public void addAreaEvaluationHistory(TrafficAreaEvaluationHistory trafficAreaEvaluationHistory) {
        baseMapper.insert(trafficAreaEvaluationHistory);
    }

    @Override
    public void updateAreaEvaluationHistory(TrafficAreaEvaluationHistory trafficAreaEvaluationHistory) {
        baseMapper.updateById(trafficAreaEvaluationHistory);
    }

    @Override
    public void deleteByIdList(List<Long> idList) {
        if (CollectionUtils.isNotEmpty(idList)) {
            baseMapper.deleteBatchIds(idList);
        }
    }

    @Override
    public TrafficAreaEvaluationHistory queryById(Long id) {
        TrafficAreaEvaluationHistory trafficAreaEvaluationHistory = baseMapper.selectById(id);
        return trafficAreaEvaluationHistory;
    }

    @Override
    public List<TrafficAreaEvaluationHistoryRankVo> queryEvaluationHistoryRankList(AreaEvaluationRankDTO dto) {
        Integer limit = dto.getLimit() == null ? 10 : dto.getLimit();
        String limitStr = String.format("LIMIT %s", limit);
        Long evaluationTypeId = dto.getEvaluationTypeId();
        MPJLambdaWrapper<TrafficAreaEvaluationHistory> queryWrapper = new MPJLambdaWrapper<>();
        queryWrapper.selectAll(TrafficAreaEvaluationHistory.class)
                .selectAs(TrafficArea::getName, TrafficAreaEvaluationHistoryRankVo::getAreaName)
                .selectAs(TrafficEvaluationType::getName, TrafficAreaEvaluationHistoryRankVo::getEvaluationTypeName)
                .leftJoin(TrafficArea.class, TrafficArea::getId, TrafficAreaEvaluationHistory::getAreaId)
                .leftJoin(TrafficEvaluationType.class, TrafficEvaluationType::getId, TrafficAreaEvaluationHistory::getEvaluationTypeId)
                .eq(TrafficAreaEvaluationHistory::getEvaluationTypeId, evaluationTypeId);

        if(evaluationTypeId!=6)   {
            queryWrapper.orderByDesc(TrafficAreaEvaluationHistory::getValue);
        }
        else{
            queryWrapper.orderByAsc(TrafficAreaEvaluationHistory::getValue);
        }
        queryWrapper.last(limitStr);
        List<TrafficAreaEvaluationHistoryRankVo> rankVOList = baseMapper.selectJoinList(TrafficAreaEvaluationHistoryRankVo.class, queryWrapper);
        return rankVOList;
    }


}
