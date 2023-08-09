package com.ruoyi.traffic.mapper.area;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.traffic.domain.area.TrafficAreaEvaluationData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * @classname: TrafficAreaEvaluationDataMapper
 * @author: ouyanghua
 * @description: 区域评价的Mapper
 * @date: 2023/7/24
 * @version: v1.0
 **/
@Mapper
public interface TrafficAreaEvaluationDataMapper extends BaseMapper<TrafficAreaEvaluationData> {
//        @Select("SELECT *, (SELECT COUNT(*) FROM traffic_area_evaluation_data t2 WHERE t2.value >= t1.value) AS rank " +
//                "FROM traffic_area_evaluation_data t1 " +
//                "WHERE evaluation_type_id = #{evaluation_type_id} " +
//                "ORDER BY value DESC")
@Select("SELECT *, RANK() OVER (ORDER BY CASE " +
        "WHEN evaluation_type_id = #{evaluation_type_id} AND #{evaluation_type_id} = 6 THEN value " +
        "ELSE value * -1 " +
        "END " +
        "#{evaluation_type_id} = 6 ASC) AS rank " +
        "FROM traffic_area_evaluation_data " +
        "ORDER BY rank "
       )

        List<TrafficAreaEvaluationData> getEvaluationRankById(@Param("evaluation_type_id") Long evaluationTypeId);

}
