package com.ruoyi.simulation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.simulation.domain.GreenWave;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 绿波组数据库访问层
 */
@Mapper
public interface GreenWaveMapper extends BaseMapper<GreenWave> {
    /**
     * 批量插入绿波信息
     * @param greenWaveList
     * @return
     */
    public int insertList(List<GreenWave> greenWaveList);

    /**
     * 获取所有绿波信息集合
     * @return
     */
    public List<GreenWave> getWaveList();
}
