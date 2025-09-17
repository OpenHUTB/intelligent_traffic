package com.ruoyi.simulation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.simulation.domain.Document;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件信息数据库访问层
 */
@Mapper
public interface DocumentMapper extends BaseMapper<Document> {
}
