package com.ruoyi.traffic.service.area.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;


import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.traffic.domain.area.TrafficArea;
import com.ruoyi.traffic.domain.area.TrafficAreaEvaluationData;
import com.ruoyi.traffic.domain.area.TrafficAreaEvaluationHistory;

import com.ruoyi.traffic.domain.evaluationType.TrafficEvaluationType;
import com.ruoyi.traffic.dto.AreaEvaluationRankDTO;
import com.ruoyi.traffic.enums.AreaEvaluationTypeEnum;
import com.ruoyi.traffic.enums.EvaluationTypeEnum;
import com.ruoyi.traffic.mapper.area.TrafficAreaEvaluationDataMapper;
import com.ruoyi.traffic.service.area.ITrafficAreaEvaluationHistoryService;
import com.ruoyi.traffic.service.area.ITrafficAreaEvaluationDataService;
import com.ruoyi.traffic.vo.TrafficAreaEvaluationDataRankVO;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

/**
 * @classname: rafficArea_Evaluation_Impl
 * @author: ouyanghua
 * @description: 区域评价的服务实现类
 * @date: 2023/7/24
 * @version: v1.0
 **/
@Service


public class TrafficAreaEvaluationDataImpl extends MPJBaseServiceImpl<TrafficAreaEvaluationDataMapper, TrafficAreaEvaluationData> implements ITrafficAreaEvaluationDataService {



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
        // 校验指标类别
        checkEvaluationType(trafficAreaEvaluationData);
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
                .year(year)
                .month(month)
                .day(day).build();
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

    @Override
    public List<TrafficAreaEvaluationDataRankVO> queryEvaluationDataRankList(AreaEvaluationRankDTO dto) {
        // 参数处理
        Integer limit = dto.getLimit() == null ? 10 : dto.getLimit();
        String limitStr = String.format("LIMIT %s", limit);
        Long evaluationTypeId = dto.getEvaluationTypeId();
        MPJLambdaWrapper<TrafficAreaEvaluationData> queryWrapper = new MPJLambdaWrapper<>();
        queryWrapper.selectAll(TrafficAreaEvaluationData.class)
                .selectAs(TrafficArea::getName, TrafficAreaEvaluationDataRankVO::getAreaName)
                .selectAs(TrafficEvaluationType::getName, TrafficAreaEvaluationDataRankVO::getEvaluationTypeName)
                .leftJoin(TrafficArea.class, TrafficArea::getId, TrafficAreaEvaluationData::getAreaId)
                .leftJoin(TrafficEvaluationType.class, TrafficEvaluationType::getId, TrafficAreaEvaluationData::getEvaluationTypeId)
                .eq(TrafficAreaEvaluationData::getEvaluationTypeId, evaluationTypeId);

        if(!evaluationTypeId.equals(AreaEvaluationTypeEnum.AVERAGE_SPEED.getEvaluationTypeId()))   {
            queryWrapper.orderByDesc(TrafficAreaEvaluationData::getValue);
        }
        else{
            queryWrapper.orderByAsc(TrafficAreaEvaluationData::getValue);
        }
        queryWrapper.last(limitStr);
        List<TrafficAreaEvaluationDataRankVO> rankVOList = baseMapper.selectJoinList(TrafficAreaEvaluationDataRankVO.class, queryWrapper);
        return rankVOList;
    }

    /**
     * 检查传入的指标数据是否符合指标类别
     * @param trafficAreaEvaluationData
     */
    private void checkEvaluationType (TrafficAreaEvaluationData trafficAreaEvaluationData) {
        if (trafficAreaEvaluationData.getEvaluationTypeId() == null) {
            throw new BaseException("指标类别不允许为空！");
        }
        if (!trafficAreaEvaluationData.getEvaluationTypeId().equals(EvaluationTypeEnum.AREA_TYPE.getEvaluationType())) {
            throw new BaseException("指标类别不是区域类型！");
        }
    }
}
