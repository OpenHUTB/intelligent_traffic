package com.ruoyi.simulation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.simulation.domain.Track;

import java.util.List;

/**
 * 车辆轨迹数据库访问层
 */
public interface TrackMapper extends BaseMapper<Track> {
    public List<String> getPlateList();
}
