package com.ruoyi.traffic.service.intersection.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.query.MPJLambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.traffic.domain.evaluationType.TrafficEvaluationType;
import com.ruoyi.traffic.domain.intersection.TrafficIntersection;
import com.ruoyi.traffic.domain.intersection.TrafficIntersectionEvaluationData;
import com.ruoyi.traffic.domain.intersection.TrafficIntersectionEvaluationHistory;
import com.ruoyi.traffic.mapper.intersection.TrafficIntersectionEvaluationDataMapper;
import com.ruoyi.traffic.service.evaluationType.ITrafficEvaluationTypeService;
import com.ruoyi.traffic.service.intersection.ITrafficIntersectionEvaluationDataService;
import com.ruoyi.traffic.service.intersection.ITrafficIntersectionEvaluationHistoryService;
import com.ruoyi.traffic.vo.TrafficIntersectionEvaluationDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.soap.SAAJResult;
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
    private ITrafficIntersectionEvaluationHistoryService trafficIntersectionEvaluationHistoryService;
    @Autowired
    private ITrafficEvaluationTypeService trafficEvaluationTypeService;
    @Override
    public List<TrafficIntersectionEvaluationData> queryList(TrafficIntersectionEvaluationData trafficEvaluationData) {
        LambdaQueryWrapper<TrafficIntersectionEvaluationData> queryWrapper = new LambdaQueryWrapper<>();
        //intersectionId
        if (StringUtils.isNotNull(trafficEvaluationData.getIntersectionId())) {
            queryWrapper.like(TrafficIntersectionEvaluationData::getIntersectionId, trafficEvaluationData.getIntersectionId());
        }
        //EvaluationTypeId
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
        if (StringUtils.isNotEmpty(idList)) {
            baseMapper.deleteBatchIds(idList);
        }
    }

    @Override
    public TrafficIntersectionEvaluationData queryById(Long id) {
        return baseMapper.selectById(id);
    }

    //按路口id删除数据
    @Override
    public void deleteEvaluationDataByIntersectionIds(List<Long> idList) {
        if (StringUtils.isNotEmpty(idList)) {
            baseMapper.deleteByIntersectionIds(idList);
        }
    }

    //联表查询
    @Override
    public List<TrafficIntersectionEvaluationDataVo> relatedQueryList(TrafficIntersectionEvaluationData trafficIntersectionEvaluationData) {
        MPJLambdaWrapper<TrafficIntersectionEvaluationData> queryWrapper = new MPJLambdaWrapper<>();
        //intersectionId
        if (StringUtils.isNotNull(trafficIntersectionEvaluationData.getIntersectionId())) {
            queryWrapper.like(TrafficIntersectionEvaluationData::getIntersectionId,trafficIntersectionEvaluationData.getIntersectionId());
        }
        //evaluationTypeId
        if (StringUtils.isNotNull(trafficIntersectionEvaluationData.getEvaluationTypeId())) {
            queryWrapper.like(TrafficIntersectionEvaluationData::getEvaluationTypeId,trafficIntersectionEvaluationData.getEvaluationTypeId());
            TrafficEvaluationType trafficEvaluationType = trafficEvaluationTypeService.queryById(trafficIntersectionEvaluationData.getEvaluationTypeId());
            //除了速度之外其余的都按字段升序
            if (trafficEvaluationType.getName().contains("速度")) {
                queryWrapper.orderByDesc(TrafficIntersectionEvaluationData::getValue);
            } else {
                queryWrapper.orderByAsc(TrafficIntersectionEvaluationData::getValue);
            }
        } else {
            //默认按数据的收集时间降序
            queryWrapper.orderByDesc(TrafficIntersectionEvaluationData::getCollectTime);
        }
        List<TrafficIntersectionEvaluationDataVo> list = baseMapper.selectJoinList(TrafficIntersectionEvaluationDataVo.class,
                queryWrapper
                        .select(TrafficIntersection::getName, TrafficIntersection::getLongitude, TrafficIntersection::getLatitude)
                        .select(TrafficEvaluationType::getName, TrafficEvaluationType::getType)
                        .select(TrafficIntersectionEvaluationData::getValue, TrafficIntersectionEvaluationData::getCollectTime)
                        .selectAs(TrafficIntersection::getName, TrafficIntersectionEvaluationDataVo::getIntersectionName)
                        .selectAs(TrafficEvaluationType::getName, TrafficIntersectionEvaluationDataVo::getEvaluationName)
                        .leftJoin(TrafficIntersection.class, TrafficIntersection::getId, TrafficIntersectionEvaluationData::getIntersectionId)
                        .leftJoin(TrafficEvaluationType.class, TrafficEvaluationType::getId, TrafficIntersectionEvaluationData::getEvaluationTypeId)
        );
        return list;
    }

    @Override
    public List<TrafficIntersectionEvaluationData> queryByIntersectionId(Long id) {
        List<TrafficIntersectionEvaluationData> list = baseMapper.queryByIntersectionId(id);
        return list;
    }
}
