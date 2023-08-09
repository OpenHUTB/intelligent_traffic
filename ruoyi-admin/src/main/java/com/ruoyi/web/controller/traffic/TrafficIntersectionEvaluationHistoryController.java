package com.ruoyi.web.controller.traffic;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.traffic.domain.intersection.TrafficIntersectionEvaluationHistory;
import com.ruoyi.traffic.service.intersection.ITrafficIntersectionEvaluationHistoryService;
import com.ruoyi.traffic.vo.TrafficIntersectionEvaluationDataVo;
import com.ruoyi.traffic.vo.TrafficIntersectionEvaluationHistoryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @classname: TrafficIntersectionEvaluationHistoryController
 * @author: ouyangdelong
 * @description: 路口历史数据的控制类
 * @date: 2023/8/9
 * @version: v1.0
 **/
@Api(value = "路口历史数据的管理", tags = "路口历史数据的管理")
@RestController
@RequestMapping("/traffic/intersectionHistory")
public class TrafficIntersectionEvaluationHistoryController extends BaseController {

    @Resource
    ITrafficIntersectionEvaluationHistoryService trafficIntersectionEvaluationHistoryService;

    @ApiOperation("分页获取路口历史数据")
    @PostMapping("/page")
    public TableDataInfo list(@ApiParam(value = "查询的参数")@RequestBody TrafficIntersectionEvaluationHistory history) {
        startPage();
        List<TrafficIntersectionEvaluationHistoryVo> list = trafficIntersectionEvaluationHistoryService.relatedQueryList(history);
        return getDataTable(list);
    }

    @ApiOperation("编辑路口历史数据")
    @PostMapping("/update")
    public AjaxResult update(@RequestBody TrafficIntersectionEvaluationHistory history) {
        trafficIntersectionEvaluationHistoryService.updateEvaluationHistory(history);
        return AjaxResult.success();
    }

    @ApiOperation("删除路口历史数据")
    @PostMapping("/delete")
    public AjaxResult delete(@ApiParam(value = "数据集ID集合", required = true)@RequestBody List<Long> idList) {
        trafficIntersectionEvaluationHistoryService.deleteEvaluationHistory(idList);
        return AjaxResult.success();
    }

    @ApiOperation("路口历史数据的详情")
    @GetMapping("findById/{id}")
    public AjaxResult findById(@PathVariable @ApiParam(name = "id")
                               @NotNull(message = "不能为空") Long id) {
        TrafficIntersectionEvaluationHistoryVo trafficIntersectionEvaluationHistoryVo = trafficIntersectionEvaluationHistoryService.queryById(id);
        return AjaxResult.success(trafficIntersectionEvaluationHistoryVo);
    }

}
