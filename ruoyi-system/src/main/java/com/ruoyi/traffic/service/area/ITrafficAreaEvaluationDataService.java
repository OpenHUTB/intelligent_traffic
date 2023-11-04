package com.ruoyi.traffic.service.area;

import com.baomidou.mybatisplus.extension.service.IService;

import com.github.yulichang.base.MPJBaseService;
import com.mathworks.toolbox.javabuilder.external.org.json.JSONArray;
import com.mathworks.toolbox.javabuilder.external.org.json.JSONException;
import com.mathworks.toolbox.javabuilder.external.org.json.JSONObject;
import com.ruoyi.traffic.domain.area.TrafficAreaEvaluationData;
import com.ruoyi.traffic.dto.AreaEvaluationRankDTO;
import com.ruoyi.traffic.vo.TrafficAreaEvaluationDataRankVO;

import java.util.List;


/**
 * @classname: ITrafficArea_Evaluation_Service
 * @author: ouyanghua
 * @description: 区域评价的实时值的服务类
 * @date: 2023/7/24
 * @version: v1.0
 **/
public interface ITrafficAreaEvaluationDataService extends MPJBaseService<TrafficAreaEvaluationData> {
    // 查询集合
    List<TrafficAreaEvaluationData> queryList(TrafficAreaEvaluationData trafficAreaEvaluationData);

    // 新增评价
    void addAreaEvaluationData(TrafficAreaEvaluationData trafficAreaEvaluationData);


    // 编辑区域评价(无需修改，数据来自接口或者采集，所以删除了修改方法)


    // 删除区域评价
    void deleteByIdList(List<Long> idList);

    //查询区域评价详情
    TrafficAreaEvaluationData queryById(Long id);

    // 查询前10名的指标排名
    List<TrafficAreaEvaluationDataRankVO> queryEvaluationDataRankList(AreaEvaluationRankDTO dto);

    //从matlab获取数据
    void addData(JSONObject jsonObject) throws JSONException;
}
