package com.ruoyi.web.controller.traffic;


import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.traffic.domain.intersection.TrafficIntersectionEvaluationData;
import com.ruoyi.traffic.service.intersection.ITrafficIntersectionEvaluationDataService;
import com.ruoyi.traffic.service.evaluationType.ITrafficEvaluationTypeService;
import com.ruoyi.traffic.service.intersection.ITrafficIntersectionService;
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
    private ITrafficIntersectionService trafficIntersectionService;
    @Resource
    private ITrafficEvaluationTypeService trafficEvaluationService;
    @Resource
    private ITrafficIntersectionEvaluationDataService trafficIntersectionEvaluationDataService;

    @ApiOperation("分页获取路口数据")
    @PostMapping("/page")
    public TableDataInfo list(@ApiParam(value = "条件查询参数") @RequestBody TrafficIntersectionEvaluationData trafficIntersectionEvaluationData) {
        startPage();
        List<TrafficIntersectionEvaluationData> list = trafficIntersectionEvaluationDataService.queryList(trafficIntersectionEvaluationData);
        return getDataTable(list);
    }

}
