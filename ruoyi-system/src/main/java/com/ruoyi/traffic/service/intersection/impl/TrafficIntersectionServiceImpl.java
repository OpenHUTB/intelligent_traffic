package com.ruoyi.traffic.service.intersection.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.traffic.domain.intersection.TrafficIntersection;
import com.ruoyi.traffic.mapper.intersection.TrafficIntersectionMapper;
import com.ruoyi.traffic.service.intersection.ITrafficIntersectionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @classname: TrafficIntersectionServiceImpl
 * @author: ouyangdelong
 * @description: 路口服务实现类
 * @date: 2023/7/24
 * @version: v1.0
 **/
@Service
public class TrafficIntersectionServiceImpl extends ServiceImpl<TrafficIntersectionMapper, TrafficIntersection>
        implements ITrafficIntersectionService {
    @Override
    public List<TrafficIntersection> queryList(TrafficIntersection trafficIntersection) {
        LambdaQueryWrapper<TrafficIntersection> queryWrapper = new LambdaQueryWrapper<>();
        //name
        if (StringUtils.isNotBlank(trafficIntersection.getName())) {
            queryWrapper.like(TrafficIntersection::getName, trafficIntersection.getName());
        }
        //longitude&latitude
        if (StringUtils.isNotBlank(trafficIntersection.getLongitude()) && StringUtils.isNotBlank(trafficIntersection.getLatitude())) {
            queryWrapper.eq(TrafficIntersection::getLongitude, trafficIntersection.getLongitude());
            queryWrapper.eq(TrafficIntersection::getLatitude, trafficIntersection.getLatitude());
        }
        List<TrafficIntersection> trafficIntersectionList = baseMapper.selectList(queryWrapper);
        return trafficIntersectionList;
    }

    @Override
    public void addIntersection(TrafficIntersection trafficIntersection) {
        if (!checkIntersectionNameUnique(trafficIntersection)) {
            throw new BaseException("路口的名子已经存在！");
        }
        baseMapper.insert(trafficIntersection);
    }

    @Override
    public void updateIntersection(TrafficIntersection trafficIntersection) {
        if (!checkIntersectionNameUnique(trafficIntersection)) {
            throw new BaseException("路口的名字已经存在！");
        }
        baseMapper.updateById(trafficIntersection);
    }

    @Override
    public void deleteIntersection(List<Long> idList) {
        if (StringUtils.isNotEmpty(idList)) {
            baseMapper.deleteBatchIds(idList);
        }
    }

    @Override
    public TrafficIntersection queryById(Long id) {
        TrafficIntersection intersection = baseMapper.selectById(id);
        return intersection;
    }

    @Override
    public TrafficIntersection queryByName(String name) {
        LambdaQueryWrapper<TrafficIntersection> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotNull(name)) {
            queryWrapper.like(TrafficIntersection::getName, name);
        }
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 检验路口的名称是否重复
     * @param trafficIntersection
     * @return
     */
    public boolean checkIntersectionNameUnique(TrafficIntersection trafficIntersection) {
        Long sectionId = StringUtils.isNull(trafficIntersection.getId()) ? -1L : trafficIntersection.getId();
        LambdaQueryWrapper<TrafficIntersection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(TrafficIntersection::getName, trafficIntersection.getName());
        TrafficIntersection intersection = baseMapper.selectOne(queryWrapper);
        if (StringUtils.isNotNull(intersection) && intersection.getId().longValue() != sectionId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}
