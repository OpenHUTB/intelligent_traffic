package com.ruoyi.simulation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.simulation.domain.SignalBase;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 信号控制信息数据库访问层接口
 */
@Mapper
public interface SignalBaselMapper extends BaseMapper<SignalBase> {
    /**
     * 查询不同交通灯对应的信控信息
     * @return
     */
    public List<SignalBase> getSignalBaseList();
}