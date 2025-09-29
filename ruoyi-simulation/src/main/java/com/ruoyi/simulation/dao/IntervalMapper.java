package com.ruoyi.simulation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.simulation.domain.Interval;
import org.apache.ibatis.annotations.Mapper;

/**
 * 间隔时间数据库访问层
 */
@Mapper
public interface IntervalMapper extends BaseMapper<Interval> {
}
