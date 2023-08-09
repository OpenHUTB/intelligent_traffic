package com.ruoyi.web.controller.traffic;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.traffic.domain.line.TrafficLine;
import com.ruoyi.traffic.dto.AreaEvaluationRankDTO;
import com.ruoyi.traffic.service.area.ITrafficAreaEvaluationDataService;
import com.ruoyi.traffic.vo.TrafficAreaEvaluationDataRankVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @classname: TrafficAreaEvaluationDataController
 * @author: chengchangli
 * @description: 区域指标实时数据控制类
 * @date: 2023/8/9
 * @version: v1.0
 **/
@Api(value = "区域指标实时数据管理类", tags = "区域指标实时数据管理类")
@RestController
@RequestMapping("/traffic/areaData/")
public class TrafficAreaEvaluationDataController extends BaseController {

    @Resource
    private ITrafficAreaEvaluationDataService trafficAreaEvaluationDataService;


    @ApiOperation("获取指标的排名")
    @PostMapping("/queryRank")
    public AjaxResult queryRank (@RequestBody AreaEvaluationRankDTO dto) {
        List<TrafficAreaEvaluationDataRankVO> rankVOList = trafficAreaEvaluationDataService.queryEvaluationDataRankList(dto);
        return AjaxResult.success(rankVOList);
    }

}
