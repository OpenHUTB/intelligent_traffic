package com.ruoyi.web.controller.traffic;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.traffic.domain.TrafficArea;
import com.ruoyi.traffic.domain.TrafficLine;
import com.ruoyi.traffic.service.ITrafficAreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @classname: TrafficAreaController
 * @author: chengchangli
 * @description: 路网的面的控制类
 * @date: 2023/7/20
 * @version: v1.0
 **/
@Api(value = "路网的面管理", tags = "路网的面管理")
@RestController
@RequestMapping("/traffic/area")
public class TrafficAreaController extends BaseController {

    @Resource
    private ITrafficAreaService trafficAreaService;

    @ApiOperation("分页获取路网的面")
    @PostMapping("/page")
    public TableDataInfo list(@ApiParam(value = "条件查询参数")@RequestBody TrafficArea trafficArea)
    {
        startPage();
        List<TrafficArea> list = trafficAreaService.queryList(trafficArea);
        return getDataTable(list);
    }

    @ApiOperation("新增路网的面")
    @PostMapping("/add")
    public AjaxResult add (@RequestBody TrafficArea trafficArea) {
        trafficAreaService.addArea(trafficArea);
        return AjaxResult.success();
    }

    @ApiOperation("编辑路网的面")
    @PostMapping("/update")
    public AjaxResult update (@RequestBody TrafficArea trafficArea) {
        trafficAreaService.updateArea(trafficArea);
        return AjaxResult.success();
    }

    @ApiOperation("删除路网的面")
    @PostMapping("delete")
    public AjaxResult delete(@ApiParam(value = "数据集ID集合", required = true) @RequestBody List<Long> ids) {
        trafficAreaService.deleteByIdList(ids);
        return AjaxResult.success();
    }

    @ApiOperation("路网的面的详情")
    @GetMapping("findById/{id}")
    public AjaxResult findById(@PathVariable @ApiParam(name = "id")
                               @NotNull(message = "不能为空") Long id) {
        TrafficArea trafficArea = trafficAreaService.queryById(id);
        return AjaxResult.success(trafficArea);
    }
}
