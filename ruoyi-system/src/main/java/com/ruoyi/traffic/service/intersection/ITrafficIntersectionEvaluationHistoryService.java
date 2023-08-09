package com.ruoyi.traffic.service.intersection;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.traffic.domain.intersection.TrafficIntersectionEvaluationData;
import com.ruoyi.traffic.domain.intersection.TrafficIntersectionEvaluationHistory;
import com.ruoyi.traffic.vo.TrafficIntersectionEvaluationDataVo;
import com.ruoyi.traffic.vo.TrafficIntersectionEvaluationHistoryVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @classname: ITrafficIntersectionEvaluationHistory
 * @author: ouyangdelong
 * @description: 历史路口评价的服务类
 * @date: 2023/8/7
 * @version: v1.0
 **/
public interface ITrafficIntersectionEvaluationHistoryService extends IService<TrafficIntersectionEvaluationHistory> {

    //查询集合
    List<TrafficIntersectionEvaluationHistory> queryList(TrafficIntersectionEvaluationHistory trafficIntersectionEvaluationHistory);

    //新增历史数据
    void addEvaluationHistory(TrafficIntersectionEvaluationHistory trafficIntersectionEvaluationHistory);

    //更新历史数据
    void updateEvaluationHistory(TrafficIntersectionEvaluationHistory trafficIntersectionEvaluationHistory);

    //删除历史数据
    void deleteEvaluationHistory(List<Long> idList);

    //查询历史数据详情
    TrafficIntersectionEvaluationHistoryVo queryById(Long id);

    //联表查询历史数据
    List<TrafficIntersectionEvaluationHistoryVo> relatedQueryList(TrafficIntersectionEvaluationHistory trafficIntersectionEvaluationHistory);

}
