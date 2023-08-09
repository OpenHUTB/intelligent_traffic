package com.ruoyi.web.controller.traffic.area;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.traffic.domain.area.TrafficAreaEvaluationData;
import com.ruoyi.traffic.mapper.area.TrafficAreaEvaluationDataMapper;
import com.ruoyi.traffic.service.area.ITrafficAreaEvaluationDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;



/**
 * @classname:  TrafficAreaEvaluationDataController
 * @author: ouyanghua
 * @description: 区域评价实时数据的控制类
 * @date: 2023/8/8
 * @version: v1.0
 **/
@Api(value = "区域评价的管理", tags = "区域评价的管理")
@RestController
@RequestMapping("/traffic/areaevaluation")
public class TrafficAreaEvaluationDataController extends BaseController {
    @Resource
    private ITrafficAreaEvaluationDataService iTrafficAreaEvaluationDataService;

    @Autowired
    private TrafficAreaEvaluationDataMapper trafficAreaEvaluationDataMapper;
    @ApiOperation("分页获取区域评价数据")
    @PostMapping("/page")
    public TableDataInfo list(@ApiParam(value = "条件查询参数")@RequestBody TrafficAreaEvaluationData trafficAreaEvaluationData)
    {
        startPage();
        List<TrafficAreaEvaluationData> list = iTrafficAreaEvaluationDataService.queryList(trafficAreaEvaluationData);
        return getDataTable(list);
    }

    @ApiOperation("新增区域评价")
    @PostMapping("/add")
    public AjaxResult add (@RequestBody TrafficAreaEvaluationData trafficAreaEvaluationData) {
        iTrafficAreaEvaluationDataService. addAreaEvaluationData(trafficAreaEvaluationData);
        return AjaxResult.success();
    }

    @ApiOperation("删除区域评价")
    @PostMapping("delete")
    public AjaxResult delete(@ApiParam(value = "数据集ID集合", required = true) @RequestBody List<Long> ids) {
        iTrafficAreaEvaluationDataService.deleteByIdList(ids);
        return AjaxResult.success();
    }

    @ApiOperation("区域评价的详情")
    @GetMapping("findById/{id}")
    public AjaxResult findById(@PathVariable @ApiParam(name = "id")
                               @NotNull(message = "不能为空") Long id) {
        TrafficAreaEvaluationData trafficAreaEvaluationData = iTrafficAreaEvaluationDataService.queryById(id);
        return AjaxResult.success(trafficAreaEvaluationData );
    }
    @ApiOperation("区域评价排名")
    @PostMapping ("rank/{evaluationTypeId}")
    public TableDataInfo Ranking(@PathVariable @ApiParam(name = "evaluationTypeId")
                                  @NotNull(message = "不能为空") Long evaluationTypeId){

        startPage();
        List<TrafficAreaEvaluationData> list = trafficAreaEvaluationDataMapper.getEvaluationRankById(evaluationTypeId);
        return getDataTable(list);
    }


}
