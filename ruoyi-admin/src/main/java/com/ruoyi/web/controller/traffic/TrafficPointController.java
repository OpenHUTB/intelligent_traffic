package com.ruoyi.web.controller.traffic;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.traffic.domain.point.TrafficPoint;
import com.ruoyi.traffic.service.point.ITrafficPointService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
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

    @ApiOperation("新增路网的点")
    @PostMapping("/add")
    public AjaxResult add (@RequestBody TrafficPoint trafficPoint) {
        trafficPointService.addPoint(trafficPoint);
        return AjaxResult.success();
    }

    @ApiOperation("编辑路网的点")
    @PostMapping("/update")
    public AjaxResult update (@RequestBody TrafficPoint trafficPoint) {
        trafficPointService.updatePoint(trafficPoint);
        return AjaxResult.success();
    }

    @ApiOperation("删除路网的点")
    @PostMapping("delete")
    public AjaxResult delete(@ApiParam(value = "数据集ID集合", required = true) @RequestBody List<Long> ids) {
        trafficPointService.deleteByIdList(ids);
        return AjaxResult.success();
    }

    @ApiOperation("路网的点的详情")
    @GetMapping("findById/{id}")
    public AjaxResult findById(@PathVariable @ApiParam(name = "id")
                               @NotNull(message = "不能为空") Long id) {
        TrafficPoint trafficPoint = trafficPointService.queryById(id);
        return AjaxResult.success(trafficPoint);
    }

}
