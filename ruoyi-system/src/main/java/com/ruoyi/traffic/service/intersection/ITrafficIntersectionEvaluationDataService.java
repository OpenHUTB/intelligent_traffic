package com.ruoyi.traffic.service.intersection;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mathworks.toolbox.javabuilder.external.org.json.JSONArray;
import com.mathworks.toolbox.javabuilder.external.org.json.JSONException;
import com.ruoyi.traffic.domain.intersection.TrafficIntersectionEvaluationData;
import com.ruoyi.traffic.vo.TrafficIntersectionEvaluationDataVo;

import java.util.List;

/**
 * @classname: ITrafficIntersectionEvaluationDataService
 * @author: ouyangdelong
 * @description: 路口评价指标实时数据服务类
 * @date: 2023/7/25
 * @version: v1.0
 **/
public interface ITrafficIntersectionEvaluationDataService extends IService<TrafficIntersectionEvaluationData> {

    //查询集合
    List<TrafficIntersectionEvaluationData> queryList(TrafficIntersectionEvaluationData trafficEvaluationData);

    //新增实时数据
    void addEvaluationData(TrafficIntersectionEvaluationData trafficEvaluationData);

    //更新实时数据
    void updateEvaluationData(TrafficIntersectionEvaluationData trafficEvaluationData);

    //删除实时数据
    void deleteEvaluationData(List<Long> idList);

    //根据路口id查询
    List<TrafficIntersectionEvaluationData> queryByIntersectionId(Long id);

    //按路口id删除数据
    void deleteEvaluationDataByIntersectionIds(List<Long> idList);

    //查询实时数据详情
    TrafficIntersectionEvaluationDataVo queryById(Long id);

    //联表查询
    List<TrafficIntersectionEvaluationDataVo> relatedQueryList(TrafficIntersectionEvaluationData trafficIntersectionEvaluationData);

    //新增路网仿真数据
    void addData(JSONArray jsonArray) throws JSONException;


}
