package com.ruoyi.web.controller.traffic;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.traffic.domain.TrafficLine;
import com.ruoyi.traffic.domain.TrafficPoint;
import com.ruoyi.traffic.service.ITrafficLineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @classname: TrafficLineController
 * @author: chengchangli
 * @description: 路网的线的管理
 * @date: 2023/7/20
 * @version: v1.0
 **/
@Api(value = "路网的线管理", tags = "路网的线管理")
@RestController
@RequestMapping("/traffic/line")
public class TrafficLineController extends BaseController {

    @Resource
    private ITrafficLineService trafficLineService;


    @ApiOperation("分页获取路网的线")
    @PostMapping("/page")
    public TableDataInfo list(@ApiParam(value = "条件查询参数")@RequestBody TrafficLine trafficLine)
    {
        startPage();
        List<TrafficLine> list = trafficLineService.queryList(trafficLine);
        return getDataTable(list);
    }

    @ApiOperation("新增路网的线")
    @PostMapping("/add")
    public AjaxResult add (@RequestBody TrafficLine trafficLine) {
        trafficLineService.addLine(trafficLine);
        return AjaxResult.success();
    }

    @ApiOperation("编辑路网的线")
    @PostMapping("/update")
    public AjaxResult update (@RequestBody TrafficLine trafficLine) {
        trafficLineService.updateLine(trafficLine);
        return AjaxResult.success();
    }

    @ApiOperation("删除路网的线")
    @PostMapping("delete")
    public AjaxResult delete(@ApiParam(value = "数据集ID集合", required = true) @RequestBody List<Long> ids) {
        trafficLineService.deleteByIdList(ids);
        return AjaxResult.success();
    }

    @ApiOperation("路网的线的详情")
    @GetMapping("findById/{id}")
    public AjaxResult findById(@PathVariable @ApiParam(name = "id")
                               @NotNull(message = "不能为空") Long id) {
        TrafficLine trafficLine = trafficLineService.queryById(id);
        return AjaxResult.success(trafficLine);
    }
}
