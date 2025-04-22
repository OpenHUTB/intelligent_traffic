package com.ruoyi.simulation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 信号控制信息数据库访问层接口
 */
@Mapper
public interface SignalBaselMapper extends BaseMapper<Signalbase> {
    /**
     * 查询不同交通灯对应的信控信息
     * @return
     */
    public List<Signalbase> getSignalBaseList();
}
