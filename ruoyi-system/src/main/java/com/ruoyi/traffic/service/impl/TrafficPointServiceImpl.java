package com.ruoyi.traffic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.traffic.domain.TrafficPoint;
import com.ruoyi.traffic.mapper.TrafficPointMapper;
import com.ruoyi.traffic.service.ITrafficPointService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @classname: TrafficPointServiceImpl
 * @author: chengchangli
 * @description: 路网的点的服务实现类
 * @date: 2023/7/18
 * @version: v1.0
 **/
@Service
public class TrafficPointServiceImpl extends ServiceImpl<TrafficPointMapper, TrafficPoint> implements ITrafficPointService {

    @Override
    public List<TrafficPoint> queryList(TrafficPoint trafficPoint) {
        LambdaQueryWrapper<TrafficPoint> queryWrapper = new LambdaQueryWrapper<>();
        // name
        if (StringUtils.isNotBlank(trafficPoint.getName())) {
            queryWrapper.like(TrafficPoint::getName, trafficPoint.getName());
        }
        // code
        if (StringUtils.isNotBlank(trafficPoint.getCode())) {
            queryWrapper.eq(TrafficPoint::getCode, trafficPoint.getCode());
        }
        // type
        if (StringUtils.isNotBlank(trafficPoint.getType())) {
            queryWrapper.eq(TrafficPoint::getType, trafficPoint.getType());
        }
        List<TrafficPoint> trafficPointList = baseMapper.selectList(queryWrapper);
        return trafficPointList;
    }
}
