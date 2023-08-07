package com.ruoyi.web.controller.traffic;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.traffic.dto.ProvinceDTO;
import com.ruoyi.traffic.service.standAdcode.ITrafficStandardAdcodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @classname: TrafficeStandardAdcodeController
 * @author: chengchangli
 * @description: 行政区划编码的控制类
 * @date: 2023/8/3
 * @version: v1.0
 **/
@Api(value = "行政区划的管理", tags = "行政区划的管理")
@RestController
@RequestMapping("/traffic/adcode")
public class TrafficStandardAdcodeController extends BaseController {

    /**
     * 此controller目前只作为行政区划方面数据的查询使用，分为省份、城市、区县、村镇四级，数据不大，先冗余在一张表
     * 主要有以下几个重要方法：
     * 1.查询省
     * 2.根据省获取市
     * 3.根据市获取区
     * 4.根据区获取村镇（街道）
     */

    @Resource
    private ITrafficStandardAdcodeService trafficStandardAdcodeService;


    @ApiOperation("获取所有的省")
    @GetMapping("province")
    public AjaxResult getAllProvince () {
        List<ProvinceDTO> provinceDTOList = trafficStandardAdcodeService.getAllProvince();
        return AjaxResult.success(provinceDTOList);
    }
}
