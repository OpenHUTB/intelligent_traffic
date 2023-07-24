package com.ruoyi.traffic.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.ruoyi.traffic.domain.TrafficArea_Evaluation;

import java.util.List;


/**
 * @classname: ITrafficArea_Evaluation_Service
 * @author: ouyanghua
 * @description: 区域评价的服务类
 * @date: 2023/7/24
 * @version: v1.0
 **/
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
