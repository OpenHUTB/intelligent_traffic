package com.ruoyi.traffic.service.point;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.traffic.domain.point.TrafficPoint;

import java.util.List;

/**
 * @classname: ITrafficPointService
 * @author: chengchangli
 * @description: 路网点的服务类
 * @date: 2023/7/18
 * @version: v1.0
 **/
public interface ITrafficPointService extends IService<TrafficPoint> {

    // 查询集合
    List<TrafficPoint> queryList(TrafficPoint trafficPoint);

    // 新增路网的点
    void addPoint(TrafficPoint trafficPoint);

    // 编辑路网的点
    void updatePoint(TrafficPoint trafficPoint);

    // 删除路网的点
    void deleteByIdList(List<Long> idList);


    // 查询路网的点的详情
    TrafficPoint queryById(Long id);
}
