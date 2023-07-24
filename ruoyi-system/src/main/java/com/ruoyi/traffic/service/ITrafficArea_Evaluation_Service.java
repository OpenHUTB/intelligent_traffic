package com.ruoyi.traffic.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.ruoyi.traffic.domain.TrafficArea_Evaluation;

import java.util.List;

public interface ITrafficArea_Evaluation_Service extends IService<TrafficArea_Evaluation> {
    // 查询集合
    List<TrafficArea_Evaluation> queryList(TrafficArea_Evaluation trafficArea_Evaluation);

    // 新增评价
    void addAreaEvaluation(TrafficArea_Evaluation trafficArea_Evaluation);

    // 编辑区域评价
    void updateAreaEvaluation(TrafficArea_Evaluation trafficArea_Evaluation);

    // 删除区域评价
    void deleteByIdList(List<Long> idList);

    //查询区域评价详情
    TrafficArea_Evaluation queryById(Long id);
}
