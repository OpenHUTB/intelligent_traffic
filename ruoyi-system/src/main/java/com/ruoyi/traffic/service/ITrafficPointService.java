package com.ruoyi.traffic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.traffic.domain.TrafficPoint;

import java.util.List;

/**
 * @classname: ITrafficPointService
 * @author: chengchangli
 * @description: 路网点的服务类
 * @date: 2023/7/18
 * @version: v1.0
 **/
public interface ITrafficPointService extends IService<TrafficPoint> {

    List<TrafficPoint> queryList(TrafficPoint trafficPoint);
}
