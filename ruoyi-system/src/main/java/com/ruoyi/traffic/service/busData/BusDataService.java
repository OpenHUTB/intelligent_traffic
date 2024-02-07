package com.ruoyi.traffic.service.busData;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.traffic.domain.busData.BusData;
import com.ruoyi.traffic.vo.BusDataVo;

import java.util.List;

public interface BusDataService extends IService<BusData> {

    // 根据车牌号查询公交车的经纬度
    List<BusDataVo> getBusGps(String busNumber);
}
