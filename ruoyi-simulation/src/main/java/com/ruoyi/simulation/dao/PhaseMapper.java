package com.ruoyi.simulation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.simulation.domain.Duration;
import org.apache.ibatis.annotations.Mapper;

/**
 * 时段信息数据库访问层
 */
@Mapper
public interface PhaseMapper extends BaseMapper<Duration> {
}
