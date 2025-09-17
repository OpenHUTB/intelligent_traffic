package com.ruoyi.simulation.service;

import com.ruoyi.simulation.util.CruiseData;
import com.ruoyi.simulation.util.Location;
import com.ruoyi.simulation.util.ResultUtil;

/**
 * 路段信息业务层
 */
public interface SectionService {
    /**
     * 执行巡游
     * @param cruiseData
     * @return
     */
    public ResultUtil<Void> cruise(CruiseData cruiseData);

    /**
     * 切换路口
     * @param location
     * @return
     */
    public ResultUtil<Void> transferJunction(Location location);
}
