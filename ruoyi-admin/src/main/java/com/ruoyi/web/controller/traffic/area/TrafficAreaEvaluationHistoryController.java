package com.ruoyi.web.controller.traffic.area;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.traffic.domain.area.TrafficAreaEvaluationData;
import com.ruoyi.traffic.domain.area.TrafficAreaEvaluationHistory;
import com.ruoyi.traffic.mapper.area.TrafficAreaEvaluationHistoryMapper;
import com.ruoyi.traffic.service.area.ITrafficAreaEvaluationHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @classname: TrafficAreaEvaluationHistoryController
 * @author: ouyanghua
 * @description: 区域评价历史记录的控制类
 * @date: 2023/8/8
 * @version: v1.0
 **/
@Api(value = "区域评价历史记录的管理", tags = "区域评价历史记录的管理")
@RestController
@RequestMapping("/traffic/areaevaluationhistory")
public class TrafficAreaEvaluationHistoryController extends BaseController {
    @Resource
    private ITrafficAreaEvaluationHistoryService iTrafficAreaEvaluationHistoryService;

    @Autowired
    private TrafficAreaEvaluationHistoryMapper trafficAreaEvaluationHistoryMapper;

    @ApiOperation("区域评价历史记录")
    @PostMapping("/page")
    public TableDataInfo list(@ApiParam(value = "条件查询参数")@RequestBody TrafficAreaEvaluationHistory trafficAreaEvaluationHistory)
    {
        startPage();
        List<TrafficAreaEvaluationHistory> list = iTrafficAreaEvaluationHistoryService.queryList(trafficAreaEvaluationHistory);
        return getDataTable(list);
    }

    @ApiOperation("新增区域评价历史记录")
    @PostMapping("/add")
    public AjaxResult add (@RequestBody TrafficAreaEvaluationHistory trafficAreaEvaluationHistory) {
        iTrafficAreaEvaluationHistoryService.addAreaEvaluationHistory(trafficAreaEvaluationHistory);
        return AjaxResult.success();
    }

    @ApiOperation("编辑区域评价历史记录")
    @PostMapping("/update")
    public AjaxResult update (@RequestBody TrafficAreaEvaluationHistory trafficAreaEvaluationHistory) {
        iTrafficAreaEvaluationHistoryService.updateAreaEvaluationHistory(trafficAreaEvaluationHistory);
        return AjaxResult.success();
    }

    @ApiOperation("删除区域评价历史记录")
    @PostMapping("delete")
    public AjaxResult delete(@ApiParam(value = "数据集ID集合", required = true) @RequestBody List<Long> ids) {
        iTrafficAreaEvaluationHistoryService.deleteByIdList(ids);
        return AjaxResult.success();
    }

    @ApiOperation("路网的面的详情")
    @GetMapping("findById/{id}")
    public AjaxResult findById(@PathVariable @ApiParam(name = "id")
                               @NotNull(message = "不能为空") Long id) {
        TrafficAreaEvaluationHistory trafficAreaEvaluationHistory =  iTrafficAreaEvaluationHistoryService.queryById(id);
        return AjaxResult.success(trafficAreaEvaluationHistory);
    }
    @ApiOperation("区域评价历史记录排名")
    @PostMapping ("rank/{evaluationTypeId}")
    public TableDataInfo Ranking(@PathVariable @ApiParam(name = "evaluationTypeId")
                                 @NotNull(message = "不能为空") Long evaluationTypeId){

        startPage();
        List<TrafficAreaEvaluationHistory> list = trafficAreaEvaluationHistoryMapper.getEvaluationRankById(evaluationTypeId);
        return getDataTable(list);
    }

}
