package com.ruoyi.traffic.service;


import com.ruoyi.traffic.domain.TrafficAreaEvaluationHistory;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * @classname: ITrafficAreaEvaluationHistoryService
 * @author: ouyanghua
 * @description: 历史区域评价的服务类
 * @date: 2023/7/24
 * @version: v1.0
 **/
@Repository
public interface ITrafficAreaEvaluationHistoryService {
    // 查询集合
    List<TrafficAreaEvaluationHistory> queryList(TrafficAreaEvaluationHistory trafficArea_evaluation_history);

    // 新增评价
    void addAreaEvaluationHistory(TrafficAreaEvaluationHistory trafficArea_evaluation_history);

    // 编辑历史区域评价
    void updateAreaEvaluationHistory(TrafficAreaEvaluationHistory trafficArea_evaluation_history);

    // 删除历史区域评价
    void deleteByIdList(List<Long> idList);

    //查询历史区域评价详情
    TrafficAreaEvaluationHistory queryById(Long id);


}
