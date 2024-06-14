package com.ruoyi.web.controller.traffic;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.traffic.service.busData.BusDataService;
import com.ruoyi.traffic.vo.BusDataVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(value = "公交车GPS数据", tags = "公交车GPS数据")
@RestController
@RequestMapping("/traffic/busData")
public class BusDataController {

    @Resource
    private BusDataService busDataService;
    @ApiOperation("获取公交车Gps数据")
    @GetMapping("/findByBusNumber/{id}")
    public AjaxResult getBusGps(@PathVariable @ApiParam(name = "id") String id) {
        List<BusDataVo> busGps = busDataService.getBusGps(id);
        return AjaxResult.success(busGps);
    }
}
