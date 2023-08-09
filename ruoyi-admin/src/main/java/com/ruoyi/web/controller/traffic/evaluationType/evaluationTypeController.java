package com.ruoyi.web.controller.traffic.evaluationType;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.traffic.domain.evaluationType.TrafficEvaluationType;
import com.ruoyi.traffic.service.evaluationType.ITrafficEvaluationTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * @classname: evaluationTypeController
 * @author: ouyanghua
 * @description: 评价指标的控制类
 * @date: 2023/8/8
 * @version: v1.0
 **/
@Api(value = "评价指标的管理", tags = "评价指标的管理")
@RestController
@RequestMapping("/traffic/evaluationType")
public class evaluationTypeController extends BaseController {
    @Resource
    private ITrafficEvaluationTypeService iTrafficEvaluationTypeService;

    @ApiOperation("分页获取指标")
    @PostMapping("/page")
    public TableDataInfo list(@ApiParam(value = "条件查询参数")@RequestBody TrafficEvaluationType trafficEvaluationType)
    {
        startPage();
        List<TrafficEvaluationType> list =iTrafficEvaluationTypeService .queryList(trafficEvaluationType);
        return getDataTable(list);
    }

    @ApiOperation("新增评价指标")
    @PostMapping("/add")
    public AjaxResult add (@RequestBody TrafficEvaluationType trafficEvaluationType) {
        iTrafficEvaluationTypeService.addEvaluationType(trafficEvaluationType);
        return AjaxResult.success();
    }

    @ApiOperation("编辑评价指标")
    @PostMapping("/update")
    public AjaxResult update (@RequestBody TrafficEvaluationType trafficEvaluationType) {
        iTrafficEvaluationTypeService.updateEvaluationType(trafficEvaluationType);
        return AjaxResult.success();
    }

    @ApiOperation("删除评价指标")
    @PostMapping("delete")
    public AjaxResult delete(@ApiParam(value = "数据集ID集合", required = true) @RequestBody List<Long> ids) {
        iTrafficEvaluationTypeService.deleteEvaluationType(ids);
        return AjaxResult.success();
    }

    @ApiOperation("路网的面的详情")
    @GetMapping("findById/{id}")
    public AjaxResult findById(@PathVariable @ApiParam(name = "id")
                               @NotNull(message = "不能为空") Long id) {
        TrafficEvaluationType trafficArea = iTrafficEvaluationTypeService.queryById(id);
        return AjaxResult.success(trafficArea);
    }

}
