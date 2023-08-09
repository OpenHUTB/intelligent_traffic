package com.ruoyi.traffic.service.area;


import com.github.yulichang.base.MPJBaseService;
import com.ruoyi.traffic.domain.area.TrafficAreaEvaluationHistory;
import com.ruoyi.traffic.dto.AreaEvaluationRankDTO;
import com.ruoyi.traffic.vo.TrafficAreaEvaluationHistoryRankVo;

import java.util.List;
/**
 * @classname: ITrafficAreaEvaluationHistoryService
 * @author: ouyanghua
 * @description: 历史区域评价的服务类
 * @date: 2023/7/24
 * @version: v1.0
 **/

public interface ITrafficAreaEvaluationHistoryService  extends MPJBaseService<TrafficAreaEvaluationHistory> {
    // 查询集合
    List<TrafficAreaEvaluationHistory> queryList(TrafficAreaEvaluationHistory trafficAreaEvaluationHistory);

    // 新增评价
    void addAreaEvaluationHistory(TrafficAreaEvaluationHistory trafficAreaEvaluationHistory);

    // 编辑历史区域评价
    void updateAreaEvaluationHistory(TrafficAreaEvaluationHistory trafficAreaEvaluationHistory);

    // 删除历史区域评价
    void deleteByIdList(List<Long> idList);

    //查询历史区域评价详情
    TrafficAreaEvaluationHistory queryById(Long id);

    // 查询前10名的指标排名
    List<TrafficAreaEvaluationHistoryRankVo> queryEvaluationHistoryRankList(AreaEvaluationRankDTO dto);

}
