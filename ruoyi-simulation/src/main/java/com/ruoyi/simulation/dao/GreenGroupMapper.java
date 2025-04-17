package com.ruoyi.simulation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.simulation.domain.GreenGroup;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 绿波组数据库访问层
 */
public interface GreenGroupMapper extends BaseMapper<GreenGroup> {
    /**
     * 查询绿波组集合
     * @return
     */
    public List<GreenGroup> getGroupList();
}
