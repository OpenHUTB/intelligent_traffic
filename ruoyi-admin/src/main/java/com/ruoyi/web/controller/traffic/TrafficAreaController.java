package com.ruoyi.web.controller.traffic;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.traffic.service.ITrafficAreaService;
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
