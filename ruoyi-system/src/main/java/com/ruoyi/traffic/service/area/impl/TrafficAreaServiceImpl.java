package com.ruoyi.traffic.service.area.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.traffic.domain.area.TrafficArea;
import com.ruoyi.traffic.mapper.area.TrafficAreaMapper;
import com.ruoyi.traffic.service.area.ITrafficAreaService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @classname: TrafficAreaServiceImpl
 * @author: chengchangli
 * @description: 路网的面的服务实现类
 * @date: 2023/7/19
 * @version: v1.0
 **/
@Service
public class TrafficAreaServiceImpl extends ServiceImpl<TrafficAreaMapper, TrafficArea> implements ITrafficAreaService {
    @Override
    public List<TrafficArea> queryList(TrafficArea trafficArea) {
        LambdaQueryWrapper<TrafficArea> queryWrapper = new LambdaQueryWrapper<>();
        // name
        if (StringUtils.isNotBlank(trafficArea.getName())) {
            queryWrapper.like(TrafficArea::getName, trafficArea.getName());
        }
        // code
        if (StringUtils.isNotBlank(trafficArea.getCode())) {
            queryWrapper.eq(TrafficArea::getCode, trafficArea.getCode());
        }
        // type
        if (StringUtils.isNotBlank(trafficArea.getLandUseType())) {
            queryWrapper.eq(TrafficArea::getLandUseType, trafficArea.getLandUseType());
        }
        List<TrafficArea> trafficAreaList = baseMapper.selectList(queryWrapper);
        return trafficAreaList;
    }

    @Override
    public void addArea(TrafficArea trafficArea) {
        if (!checkAreaNameUnique(trafficArea)) {
            throw new BaseException("路网的面的名字已存在！");
        }
        if (!checkAreaCodeUnique(trafficArea)) {
            throw new BaseException("路网的面的编码已存在！");
        }
        baseMapper.insert(trafficArea);
    }

    @Override
    public void updateArea(TrafficArea trafficArea) {
        if (!checkAreaNameUnique(trafficArea)) {
            throw new BaseException("路网的面的名字已存在！");
        }
        if (!checkAreaCodeUnique(trafficArea)) {
            throw new BaseException("路网的面的编码已存在！");
        }
        baseMapper.updateById(trafficArea);
    }

    @Override
    public void deleteByIdList(List<Long> idList) {
        if (CollectionUtils.isNotEmpty(idList)) {
            baseMapper.deleteBatchIds(idList);
        }
    }

    @Override
    public TrafficArea queryById(Long id) {
        TrafficArea trafficArea = baseMapper.selectById(id);
        return trafficArea;
    }

    /**
     * 校验路网的点的名称是否重复
     * @param trafficArea
     * @return
     */
    private boolean checkAreaNameUnique(TrafficArea trafficArea) {
        Long areaId = StringUtils.isNull(trafficArea.getId()) ? -1L : trafficArea.getId();
        LambdaQueryWrapper<TrafficArea> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TrafficArea::getName, trafficArea.getName());
        TrafficArea info = baseMapper.selectOne(queryWrapper);
        if (StringUtils.isNotNull(info) && info.getId().longValue() != areaId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验路网的点的名称是否重复
     * @param trafficArea
     * @return
     */
    private boolean checkAreaCodeUnique(TrafficArea trafficArea) {
        Long areaId = StringUtils.isNull(trafficArea.getId()) ? -1L : trafficArea.getId();
        LambdaQueryWrapper<TrafficArea> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TrafficArea::getCode, trafficArea.getCode());
        TrafficArea info = baseMapper.selectOne(queryWrapper);
        if (StringUtils.isNotNull(info) && info.getId().longValue() != areaId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}
