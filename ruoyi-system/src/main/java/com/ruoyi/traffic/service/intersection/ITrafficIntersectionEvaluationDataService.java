package com.ruoyi.traffic.service.intersection;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.traffic.domain.intersection.TrafficIntersectionEvaluationData;

import java.util.List;

/**
 * @classname: TrafficEvaluationDataService
 * @author: ouyangdelong
 * @description: 路口评价指标实时数据服务类
 * @date: 2023/7/25
 * @version: v1.0
 **/
public interface ITrafficIntersectionEvaluationDataService extends IService<TrafficIntersectionEvaluationData> {

    //查询集合
    List<TrafficIntersectionEvaluationData> queryList(TrafficIntersectionEvaluationData trafficEvaluationData);

    //新增实时数据
    void addEvaluationData(TrafficIntersectionEvaluationData trafficEvaluationData);

    //更新实时数据
    void updateEvaluationData(TrafficIntersectionEvaluationData trafficEvaluationData);

    //删除实时数据
    void deleteEvaluationData(List<Long> idList);

    //查询实时数据详情
    TrafficIntersectionEvaluationData queryById(Long id);
}
