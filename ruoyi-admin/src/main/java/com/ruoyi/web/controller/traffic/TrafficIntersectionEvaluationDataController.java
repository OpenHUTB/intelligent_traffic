package com.ruoyi.web.controller.traffic;


import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.traffic.domain.evaluationType.TrafficEvaluationType;
import com.ruoyi.traffic.domain.intersection.TrafficIntersection;
import com.ruoyi.traffic.domain.intersection.TrafficIntersectionEvaluationData;
import com.ruoyi.traffic.service.intersection.ITrafficIntersectionEvaluationDataService;
import com.ruoyi.traffic.service.evaluationType.ITrafficEvaluationTypeService;
import com.ruoyi.traffic.service.intersection.ITrafficIntersectionService;
import com.ruoyi.traffic.vo.TrafficIntersectionEvaluationDataVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @classname: TrafficIntersectionEvaluationController
 * @author: ouyangdelong
 * @description: 路口数据的控制类
 * @date: 2023/7/24
 * @version: v1.0
 **/
@Api(value = "路口数据的管理", tags = "路口数据的管理")
@RestController
@RequestMapping("/traffic/intersectionData")
public class TrafficIntersectionEvaluationDataController extends BaseController {

    @Resource
    private ITrafficIntersectionEvaluationDataService trafficIntersectionEvaluationDataService;

    @ApiOperation("分页获取路口数据")
    @PostMapping("/page")
    public TableDataInfo list(@ApiParam(value = "条件查询参数") @RequestBody TrafficIntersectionEvaluationData trafficIntersectionEvaluationData) {
        startPage();
        List<TrafficIntersectionEvaluationDataVo> list = trafficIntersectionEvaluationDataService.relatedQueryList(trafficIntersectionEvaluationData);
        return getDataTable(list);
    }

    @ApiOperation("新增路口数据")
    @PostMapping("/add")
    public AjaxResult add(@RequestBody TrafficIntersectionEvaluationData trafficIntersectionEvaluationData) {
        trafficIntersectionEvaluationDataService.addEvaluationData(trafficIntersectionEvaluationData);
        return AjaxResult.success();
    }

    @ApiOperation("修改路口数据")
    @PostMapping("/update")
    public AjaxResult update(@RequestBody TrafficIntersectionEvaluationData trafficIntersectionEvaluationData) {
        trafficIntersectionEvaluationDataService.updateEvaluationData(trafficIntersectionEvaluationData);
        return AjaxResult.success();
    }

    @ApiOperation("删除路口数据")
    @PostMapping("/delete")
    public AjaxResult delete(@ApiParam(value = "数据集ID集合", required = true)@RequestBody List<Long> idList) {
        trafficIntersectionEvaluationDataService.deleteEvaluationData(idList);
        return AjaxResult.success();
    }

    @ApiOperation("路口数据的详情")
    @GetMapping("findById/{id}")
    public AjaxResult findById(@PathVariable @ApiParam(name = "id")
                               @NotNull(message = "不能为空") Long id) {
        TrafficIntersectionEvaluationData trafficIntersectionEvaluationData = trafficIntersectionEvaluationDataService.queryById(id);
        return AjaxResult.success(trafficIntersectionEvaluationData);
    }



}
