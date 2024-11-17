package com.ruoyi.simulation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.simulation.domain.Speed;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 雷达信息业务层接口
 */
@Mapper
public interface SpeedMapper extends BaseMapper<Speed> {
    /**
     * 获取不同道路的历史平均车速
     * @return
     */
    public List<Speed> getAverageSpeedList();

    /**
     * 批量添加平均速度
     * @param speedList
     * @return
     */
    public int addAverageSpeedList(List<Speed> speedList);
}
