package com.ruoyi.simulation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.simulation.domain.TrafficLight;
import org.apache.ibatis.annotations.Mapper;

/**
 * 交通灯信息数据库访问层
 */
@Mapper
public interface TrafficLightMapper extends BaseMapper<TrafficLight> {
}
