package com.ruoyi.traffic.service.line.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.traffic.domain.line.TrafficLine;
import com.ruoyi.traffic.mapper.line.TrafficLineMapper;
import com.ruoyi.traffic.service.line.ITrafficLineService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @classname: TrafficLineServiceImpl
 * @author: chengchangli
 * @description: 路网的线的服务实现类
 * @date: 2023/7/19
 * @version: v1.0
 **/
@Service
public class TrafficLineServiceImpl extends ServiceImpl<TrafficLineMapper, TrafficLine> implements ITrafficLineService {
    @Override
    public List<TrafficLine> queryList(TrafficLine trafficLine) {
        LambdaQueryWrapper<TrafficLine> queryWrapper = new LambdaQueryWrapper<>();
        // name
        if (StringUtils.isNotBlank(trafficLine.getName())) {
            queryWrapper.like(TrafficLine::getName, trafficLine.getName());
        }
        List<TrafficLine> trafficLineList = baseMapper.selectList(queryWrapper);
        return trafficLineList;
    }

    @Override
    public void addLine(TrafficLine trafficLine) {
        if (!checkLineNameUnique(trafficLine)) {
            throw new BaseException("线路的名称已存在！");
        }
        baseMapper.insert(trafficLine);
    }

    @Override
    public void updateLine(TrafficLine trafficLine) {
        if (!checkLineNameUnique(trafficLine)) {
            throw new BaseException("线路的名称已存在！");
        }
        baseMapper.updateById(trafficLine);
    }

    @Override
    public void deleteByIdList(List<Long> idList) {
        if (CollectionUtils.isNotEmpty(idList)) {
            baseMapper.deleteBatchIds(idList);
        }
    }

    @Override
    public TrafficLine queryById(Long id) {
        TrafficLine trafficLine = baseMapper.selectById(id);
        return trafficLine;
    }

    /**
     * 校验路网的线的名称是否重复
     * @param trafficLine
     * @return
     */
    private boolean checkLineNameUnique(TrafficLine trafficLine) {
        Long lineId = StringUtils.isNull(trafficLine.getId()) ? -1L : trafficLine.getId();
        LambdaQueryWrapper<TrafficLine> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TrafficLine::getName, trafficLine.getName());
        TrafficLine info = baseMapper.selectOne(queryWrapper);
        if (StringUtils.isNotNull(info) && info.getId().longValue() != lineId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}
