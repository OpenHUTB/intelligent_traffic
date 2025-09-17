package com.ruoyi.simulation.service;

import com.ruoyi.simulation.util.Location;
import com.ruoyi.simulation.util.ResultUtil;

import java.util.List;

/**
 * 绿波信息业务层
 */
public interface GreenWaveService {
    /**
     * 设置绿波相位数据集
     * @param locationList
     * @return
     */
    public ResultUtil<Void> addGreenWave(List<Location> locationList);
}
