package com.ruoyi.traffic.service.point.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.traffic.domain.point.TrafficPoint;
import com.ruoyi.traffic.mapper.point.TrafficPointMapper;
import com.ruoyi.traffic.service.point.ITrafficPointService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Override
    public void addPoint(TrafficPoint trafficPoint) {
        // 点名称不能重复
        boolean nameUnique = checkPointNameUnique(trafficPoint);
        if (!nameUnique) {
            throw new BaseException("路网点的名称不能重复");
        }
        // 节点code不能重复
        boolean codeUnique = checkPointCodeUnique(trafficPoint);
        if (!codeUnique) {
            throw new BaseException("路网点的编码不能重复");
        }
        baseMapper.insert(trafficPoint);
    }

    @Override
    public void updatePoint(TrafficPoint trafficPoint) {
        // 点名称不能重复
        boolean nameUnique = checkPointNameUnique(trafficPoint);
        if (!nameUnique) {
            throw new BaseException("路网点的名称不能重复");
        }
        // 节点code不能重复
        boolean codeUnique = checkPointCodeUnique(trafficPoint);
        if (!codeUnique) {
            throw new BaseException("路网点的编码不能重复");
        }
        baseMapper.updateById(trafficPoint);
    }

    @Override
    public void deleteByIdList(List<Long> idList) {
        if (CollectionUtils.isNotEmpty(idList)) {
            baseMapper.deleteBatchIds(idList);
        }
    }

    @Override
    public TrafficPoint queryById(Long id) {
        TrafficPoint trafficPoint = baseMapper.selectById(id);
        return trafficPoint;
    }


    /**
     * 校验路网的点的名称是否重复
     * @param trafficPoint
     * @return
     */
    private boolean checkPointNameUnique(TrafficPoint trafficPoint) {
        Long pointId = StringUtils.isNull(trafficPoint.getId()) ? -1L : trafficPoint.getId();
        LambdaQueryWrapper<TrafficPoint> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TrafficPoint::getName, trafficPoint.getName());
        TrafficPoint info = baseMapper.selectOne(queryWrapper);
        if (StringUtils.isNotNull(info) && info.getId().longValue() != pointId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验路网的点的编码是否重复
     * @param trafficPoint
     * @return
     */
    private boolean checkPointCodeUnique(TrafficPoint trafficPoint) {
        Long pointId = StringUtils.isNull(trafficPoint.getId()) ? -1L : trafficPoint.getId();
        LambdaQueryWrapper<TrafficPoint> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TrafficPoint::getCode, trafficPoint.getCode());
        TrafficPoint info = baseMapper.selectOne(queryWrapper);
        if (StringUtils.isNotNull(info) && info.getId().longValue() != pointId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}
