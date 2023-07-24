package com.ruoyi.web.controller.traffic;


import com.ruoyi.traffic.service.ITrafficEvaluationService;
import com.ruoyi.traffic.service.ITrafficIntersectionService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @classname: TrafficIntersectionEvaluationController
 * @author: ouyangdelong
 * @description: 路口和评价指标的控制类
 * @date: 2023/7/24
 * @version: v1.0
 **/
@Api(value = "路口的管理", tags = "路口的管理")
@RestController
@RequestMapping("/traffic/intersection")
public class TrafficIntersectionEvaluationController {

    @Resource
    private ITrafficIntersectionService TrafficIntersectionService;
    @Resource
    private ITrafficEvaluationService trafficEvaluationService;
}
