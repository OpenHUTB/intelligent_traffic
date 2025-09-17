package com.ruoyi.simulation.controller;

import com.ruoyi.simulation.service.GreenWaveService;
import com.ruoyi.simulation.util.Location;
import com.ruoyi.simulation.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 绿波信息控制层
 */
@RestController
@RequestMapping("simulation/greenWave")
public class GreenWaveController {
    @Autowired
    private GreenWaveService greenWaveService;
    /**
     * 设置绿波相位数据集
     * @param locationList 路口坐标集合
     * @return
     */
    @PostMapping("addGreenWave")
    public ResultUtil<Void> addGreenWave(@RequestBody List<Location> locationList){
        return this.greenWaveService.addGreenWave(locationList);
    }
}
