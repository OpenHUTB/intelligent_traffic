package com.ruoyi.traffic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.traffic.domain.TrafficEvaluationType;

import java.util.List;

/**
 * @classname: TrafficEvaluationService
 * @author: ouyangdelong
 * @description: 路口评价指标类型服务类
 * @date: 2023/7/24
 * @version: v1.0
 **/
public interface ITrafficEvaluationTypeService extends IService<TrafficEvaluationType> {

    //查询集合
    List<TrafficEvaluationType> queryList(TrafficEvaluationType trafficEvaluationType);

    //添加评价指标
    void addEvaluationType(TrafficEvaluationType trafficEvaluationType);

    //更改评价指标
    void updateEvaluationType(TrafficEvaluationType trafficEvaluationType);

    //删除评价指标
    void deleteEvaluationType(List<Long> idList);

    //查询指标详情
    TrafficEvaluationType queryById(Long id);
}
