package com.ruoyi.traffic.service.intersection.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.mathworks.toolbox.javabuilder.external.org.json.JSONArray;
import com.mathworks.toolbox.javabuilder.external.org.json.JSONException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.traffic.domain.evaluationType.TrafficEvaluationType;
import com.ruoyi.traffic.domain.intersection.TrafficIntersection;
import com.ruoyi.traffic.domain.intersection.TrafficIntersectionEvaluationData;
import com.ruoyi.traffic.domain.intersection.TrafficIntersectionEvaluationHistory;
import com.ruoyi.traffic.enums.EvaluationTypeEnum;
import com.ruoyi.traffic.mapper.intersection.TrafficIntersectionEvaluationDataMapper;
import com.ruoyi.traffic.service.evaluationType.ITrafficEvaluationTypeService;
import com.ruoyi.traffic.service.intersection.ITrafficIntersectionEvaluationDataService;
import com.ruoyi.traffic.service.intersection.ITrafficIntersectionEvaluationHistoryService;
import com.ruoyi.traffic.service.intersection.ITrafficIntersectionService;
import com.ruoyi.traffic.vo.TrafficIntersectionEvaluationDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
    @Autowired
    private ITrafficIntersectionEvaluationDataService dataService;
    @Autowired
    private ITrafficIntersectionService intersectionService;
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
        //若调用者不提供数据采集时间，则默认系统当前时间
        if (trafficEvaluationData.getCollectTime() == null) {
            trafficEvaluationData.setCollectTime(new Date());
        }
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
                .year(year)
                .month(month)
                .day(day).build();
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
    public TrafficIntersectionEvaluationDataVo queryById(Long id) {
        MPJLambdaWrapper<TrafficIntersectionEvaluationData> queryWrapper = new MPJLambdaWrapper<>();
        queryWrapper.like(TrafficIntersectionEvaluationData::getId, id);
        TrafficIntersectionEvaluationDataVo vo = baseMapper.selectJoinOne(TrafficIntersectionEvaluationDataVo.class,
                queryWrapper
                        .select(TrafficIntersection::getName, TrafficIntersection::getLongitude, TrafficIntersection::getLatitude)
                        .select(TrafficEvaluationType::getName, TrafficEvaluationType::getType)
                        .select(TrafficIntersectionEvaluationData::getValue, TrafficIntersectionEvaluationData::getCollectTime)
                        .selectAs(TrafficIntersection::getName, TrafficIntersectionEvaluationDataVo::getIntersectionName)
                        .selectAs(TrafficEvaluationType::getName, TrafficIntersectionEvaluationDataVo::getEvaluationName)
                        .leftJoin(TrafficIntersection.class, TrafficIntersection::getId, TrafficIntersectionEvaluationData::getIntersectionId)
                        .leftJoin(TrafficEvaluationType.class, TrafficEvaluationType::getId, TrafficIntersectionEvaluationData::getEvaluationTypeId)
        );
        return vo;
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
            //除了速度之外其余的都按字段升序(路口的指标现在暂时还没有包含速度的，如果后期增加，可以新增枚举类来代替查库，这种指标类别基本创建后不会修改)
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

    /**
     * 新增路网仿真数据
     * @param jsonArray
     */
    @Transactional
    @Override
    public void addData(JSONArray jsonArray) throws JSONException {
        List<TrafficIntersectionEvaluationData> dataList = new ArrayList<>();
        // 指标类型
        String typeId = jsonArray.getJSONObject(0).getString("id");
        for (int i = 1; i < jsonArray.length(); i++) {
            TrafficIntersectionEvaluationData data = new TrafficIntersectionEvaluationData();
            // 路口名称
            String name = jsonArray.getJSONObject(i).getString("name");

            // 路口指标
            String evaluation = jsonArray.getJSONObject(i).getString("datatype");

            // 数据
            Object value = jsonArray.getJSONObject(i).get("volume");

            // 路口相对坐标x, y
            String x = jsonArray.getJSONObject(i).getJSONObject("Coordinate").getString("lat");
            String y = jsonArray.getJSONObject(i).getJSONObject("Coordinate").getString("lon");

            // 判断该路口是否已存在
            TrafficIntersection trafficIntersection = intersectionService.queryByName(name);
            if (trafficIntersection== null) {
                // 不存在该路口，则添加路口到数据库中
                TrafficIntersection intersection = new TrafficIntersection();
                intersection.setName(name);
                intersection.setLatitude(x);
                intersection.setLongitude(y);
                intersectionService.addIntersection(intersection);
                data.setIntersectionId(intersection.getId());
            } else {
                data.setIntersectionId(trafficIntersection.getId());
            }

            // 判断该指标是否存在
            TrafficEvaluationType type = trafficEvaluationTypeService.queryByName(evaluation);
            if (type == null) {
                // 不存在该指标，添加到数据库中
                TrafficEvaluationType evaluationType = new TrafficEvaluationType();
                evaluationType.setName(evaluation);
                evaluationType.setType(typeId);
                evaluationType.setRemark(Objects.requireNonNull(EvaluationTypeEnum.getEnumByType(typeId)).getDesc());
                trafficEvaluationTypeService.addEvaluationType(evaluationType);
                data.setEvaluationTypeId(evaluationType.getId());
            } else {
                data.setEvaluationTypeId(type.getId());
            }
            data.setValue(new BigDecimal(value.toString()));
            data.setCollectTime(new Date());
            dataList.add(data);
        }
        dataService.saveBatch(dataList);
    }
}
