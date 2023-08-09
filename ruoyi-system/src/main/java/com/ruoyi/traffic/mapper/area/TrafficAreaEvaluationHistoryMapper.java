package com.ruoyi.traffic.mapper.area;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.ruoyi.traffic.domain.area.TrafficAreaEvaluationHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * @classname: TrafficAreaEvaluationHistoryMapper
 * @author: ouyanghua
 * @description: 历史区域评价的Mapper
 * @date: 2023/7/24
 * @version: v1.0
 **/
@Mapper
public interface TrafficAreaEvaluationHistoryMapper extends BaseMapper<TrafficAreaEvaluationHistory> {
//   @Select("SELECT *, RANK() OVER (ORDER BY value DESC) AS rank " +
//           "FROM traffic_area_evaluation_history " +
//           "WHERE evaluation_type_id = #{evaluation_type_id} " +
//           "ORDER BY value DESC " +
//           "LIMIT 20")
@Select( "SELECT VALUE RANK() OVER ( ORDER BY VALUE DESC ) AS myrank " +
        "FROM traffic_area_evaluation_history "+
    "WHERE evaluation_type_id =#{evaluation_type_id} LIMIT 20"
           )


    List<TrafficAreaEvaluationHistory> getEvaluationRankById(@Param("evaluation_type_id") Long evaluationTypeId);
}
