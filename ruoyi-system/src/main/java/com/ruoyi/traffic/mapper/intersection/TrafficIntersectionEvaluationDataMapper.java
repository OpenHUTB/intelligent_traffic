package com.ruoyi.traffic.mapper.intersection;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.MPJBaseMapper;
import com.ruoyi.traffic.domain.intersection.TrafficIntersectionEvaluationData;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.annotation.ManagedBean;
import java.util.List;

/**
 * @classname: TrafficIntersectionEvaluationDataMapper
 * @author: ouyangdelong
 * @description: 路口评价指标实时数据Mapper类
 * @date: 2023/7/25
 * @version: v1.0
 **/
public interface TrafficIntersectionEvaluationDataMapper extends MPJBaseMapper<TrafficIntersectionEvaluationData> {

    //按路口id删除数据
    void deleteByIntersectionIds(@Param("idList") List<Long> idList);

    //按路口id查询数据
    List<TrafficIntersectionEvaluationData> queryByIntersectionId(@Param("id") Long id);
}
