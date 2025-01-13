package com.ruoyi.simulation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.simulation.domain.Junction;
import org.apache.ibatis.annotations.Mapper;

/**
 * 路口信息数据库访问层
 */
@Mapper
public interface JunctionMapper  extends BaseMapper<Junction> {
}
