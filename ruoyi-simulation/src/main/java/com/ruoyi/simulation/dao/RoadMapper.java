package com.ruoyi.simulation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.simulation.domain.Road;
import com.ruoyi.simulation.domain.Speed;
import org.apache.ibatis.annotations.Mapper;

/**
 * 道路信息数据访问层
 */
@Mapper
public interface RoadMapper extends BaseMapper<Road> {
}
