package com.ruoyi.simulation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.simulation.domain.Prediction;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 预测信息数据库访问层
 */
@Mapper
public interface PredictionMapper extends BaseMapper<Prediction> {
    public List<Prediction> getNearestList();
}
