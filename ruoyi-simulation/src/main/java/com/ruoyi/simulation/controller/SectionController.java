package com.ruoyi.simulation.controller;

import com.ruoyi.simulation.domain.Section;
import com.ruoyi.simulation.service.SectionService;
import com.ruoyi.simulation.util.CruiseData;
import com.ruoyi.simulation.util.Location;
import com.ruoyi.simulation.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 路段信息控制层
 */
@RestController
@RequestMapping("simulation/section")
public class SectionController {
    @Autowired
    private SectionService sectionService;
    /**
     * 执行巡游
     * @param cruiseData
     * @return
     */
    @PostMapping("cruise")
    public ResultUtil<Void> cruise(@RequestBody CruiseData cruiseData){
        return this.sectionService.cruise(cruiseData);
    }
    /**
     * 切换路口
     * @param location
     * @return
     */
    @PostMapping("transferJunction")
    public ResultUtil<Void> transferJunction(@RequestBody Location location){
        return this.sectionService.transferJunction(location);
    }
}
