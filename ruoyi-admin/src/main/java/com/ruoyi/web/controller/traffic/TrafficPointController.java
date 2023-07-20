package com.ruoyi.web.controller.traffic;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.traffic.domain.TrafficPoint;
import com.ruoyi.traffic.service.ITrafficPointService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @classname: TrafficPointController
 * @author: chengchangli
 * @description: 路网的点的Controller类
 * @date: 2023/7/18
 * @version: v1.0
 **/
@Api(value = "路网的点管理", tags = "路网的点管理")
@RestController
@RequestMapping("/traffic/point")
public class TrafficPointController extends BaseController {

    @Resource
    private ITrafficPointService trafficPointService;

    @ApiOperation("分页获取路网的点")
    @PostMapping("/page")
    public TableDataInfo list(@ApiParam(value = "条件查询参数")@RequestBody TrafficPoint trafficPoint)
    {
        startPage();
        List<TrafficPoint> list = trafficPointService.queryList(trafficPoint);
        return getDataTable(list);
    }

}
