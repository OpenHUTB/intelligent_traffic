package com.ruoyi.web.controller.traffic;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.traffic.service.ITrafficAreaService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
}
