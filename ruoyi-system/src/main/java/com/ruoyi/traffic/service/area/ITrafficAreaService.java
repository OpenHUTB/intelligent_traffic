package com.ruoyi.traffic.service.area;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.traffic.domain.area.TrafficArea;

import java.util.List;

/**
 * @classname: ITrafficAreaService
 * @author: chengchangli
 * @description: 路网的面的服务类
 * @date: 2023/7/19
 * @version: v1.0
 **/
public interface ITrafficAreaService extends IService<TrafficArea> {

    // 查询集合
    List<TrafficArea> queryList(TrafficArea trafficArea);

    // 新增路网的线
    void addArea(TrafficArea trafficArea);

    // 编辑路网的线
    void updateArea(TrafficArea trafficArea);

    // 删除路网的线
    void deleteByIdList(List<Long> idList);

    // 查询路网的线的详情
    TrafficArea queryById(Long id);
}
