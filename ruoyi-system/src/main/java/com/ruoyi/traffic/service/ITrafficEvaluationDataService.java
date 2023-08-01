package com.ruoyi.traffic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.traffic.domain.TrafficEvaluationData;

import java.util.List;

/**
 * @classname: TrafficEvaluationDataService
 * @author: ouyangdelong
 * @description: 路口评价指标实时数据服务类
 * @date: 2023/7/25
 * @version: v1.0
 **/
public interface ITrafficEvaluationDataService extends IService<TrafficEvaluationData> {

    //查询集合
    List<TrafficEvaluationData> queryList(TrafficEvaluationData trafficEvaluationData);

    //新增实时数据
    void addEvaluationData(TrafficEvaluationData trafficEvaluationData);

    //更新实时数据
    void updateEvaluationData(TrafficEvaluationData trafficEvaluationData);

    //删除实时数据
    void deleteEvaluationData(List<Long> idList);

    //查询实时数据详情
    TrafficEvaluationData queryById(Long id);
}
