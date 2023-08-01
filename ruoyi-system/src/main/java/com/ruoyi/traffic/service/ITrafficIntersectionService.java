package com.ruoyi.traffic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.traffic.domain.TrafficIntersection;

import java.util.List;

/**
 * @classname: TrafficIntersectionService
 * @author: ouyangdelong
 * @description: 路口服务类
 * @date: 2023/7/24
 * @version: v1.0
 **/
public interface ITrafficIntersectionService extends IService<TrafficIntersection> {

    //查询集合
    List<TrafficIntersection> quertList(TrafficIntersection trafficIntersection);

    //新增交通路口
    void addIntersection(TrafficIntersection trafficIntersection);

    //编辑交通路口
    void updateIntersection(TrafficIntersection trafficIntersection);

    //删除交通路口
    void deleteIntersection(List<Long> idList);

    //查询交通路口的详情
    TrafficIntersection queryById(Long id);

}
