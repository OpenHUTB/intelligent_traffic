package com.ruoyi.traffic.service.line;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.traffic.domain.line.TrafficLine;

import java.util.List;

/**
 * @classname: ITrafficLineService
 * @author: chengchangli
 * @description: 路网的线的服务类
 * @date: 2023/7/19
 * @version: v1.0
 **/
public interface ITrafficLineService extends IService<TrafficLine> {

    // 查询集合
    List<TrafficLine> queryList(TrafficLine trafficLine);

    // 新增路网的线
    void addLine(TrafficLine trafficLine);

    // 编辑路网的线
    void updateLine(TrafficLine trafficLine);

    // 删除路网的线
    void deleteByIdList(List<Long> idList);

    // 查询路网的线的详情
    TrafficLine queryById(Long id);
}
