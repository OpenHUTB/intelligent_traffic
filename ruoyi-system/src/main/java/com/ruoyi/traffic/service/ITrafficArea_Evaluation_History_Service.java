package com.ruoyi.traffic.service;


import com.ruoyi.traffic.domain.TrafficArea_Evaluation_History;

import java.util.List;

public interface ITrafficArea_Evaluation_History_Service {
    // 查询集合
    List<TrafficArea_Evaluation_History> queryList(TrafficArea_Evaluation_History trafficArea_evaluation_history);

    // 新增评价
    void addAreaEvaluationHistory(TrafficArea_Evaluation_History trafficArea_evaluation_history);

    // 编辑历史区域评价
    void updateAreaEvaluationHistory(TrafficArea_Evaluation_History trafficArea_evaluation_history);

    // 删除历史区域评价
    void deleteByIdList(List<Long> idList);

    //查询历史区域评价详情
    TrafficArea_Evaluation_History queryById(Long id);


}
