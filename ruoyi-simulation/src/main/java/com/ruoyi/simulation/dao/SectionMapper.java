package com.ruoyi.simulation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.simulation.domain.Section;
import org.apache.ibatis.annotations.Mapper;

/**
 * 坐标信息数据库访问层
 */
@Mapper
public interface SectionMapper extends BaseMapper<Section> {
}
