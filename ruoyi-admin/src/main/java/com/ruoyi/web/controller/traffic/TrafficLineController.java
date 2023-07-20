package com.ruoyi.web.controller.traffic;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.traffic.service.ITrafficLineService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
}
