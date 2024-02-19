package com.ruoyi.traffic.service.evaluationType.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.traffic.domain.evaluationType.TrafficEvaluationType;
import com.ruoyi.traffic.mapper.evaluationType.TrafficEvaluationTypeMapper;
import com.ruoyi.traffic.service.evaluationType.ITrafficEvaluationTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @classname: TrafficEvaluationTypeServiceImpl
 * @author: ouyangdelong
 * @description: 路口评价指标类型服务实现类
 * @date: 2023/7/24
 * @version: v1.0
 **/
@Service
public class TrafficEvaluationTypeServiceImpl extends ServiceImpl<TrafficEvaluationTypeMapper, TrafficEvaluationType>
        implements ITrafficEvaluationTypeService {
    @Override
    public List<TrafficEvaluationType> queryList(TrafficEvaluationType trafficEvaluationType) {
        LambdaQueryWrapper<TrafficEvaluationType> queryWrapper = new LambdaQueryWrapper<>();
        //name
        if (StringUtils.isNotBlank(trafficEvaluationType.getName())) {
            queryWrapper.like(TrafficEvaluationType::getName, trafficEvaluationType.getName());
        }
        //type
        if (StringUtils.isNotBlank(trafficEvaluationType.getType())) {
            queryWrapper.like(TrafficEvaluationType::getType, trafficEvaluationType.getType());
        }
        List<TrafficEvaluationType> trafficEvaluationTypeList = baseMapper.selectList(queryWrapper);
        return trafficEvaluationTypeList;
    }

    @Override
    public void addEvaluationType(TrafficEvaluationType trafficEvaluationType) {
        if (!checkEvaluationNameUnique(trafficEvaluationType)) {
            throw new BaseException("评价指标的类型已存在！");
        }
        baseMapper.insert(trafficEvaluationType);
    }

    @Override
    public void updateEvaluationType(TrafficEvaluationType trafficEvaluationType) {
        if (!checkEvaluationNameUnique(trafficEvaluationType)) {
            throw new BaseException("评价指标的名字已存在！");
        }
        baseMapper.updateById(trafficEvaluationType);
    }

    @Override
    public void deleteEvaluationType(List<Long> idList) {
        if (StringUtils.isNotEmpty(idList)) {
            baseMapper.deleteBatchIds(idList);
        }
    }

    @Override
    public void deleteTypeById(Long id) {
        baseMapper.deleteById(id);
    }

    @Override
    public TrafficEvaluationType queryById(Long id) {
        TrafficEvaluationType trafficEvaluationType = baseMapper.selectById(id);
        return trafficEvaluationType;
    }

    @Override
    public TrafficEvaluationType queryByName(String name) {
        LambdaQueryWrapper<TrafficEvaluationType> queryWrapper =  new LambdaQueryWrapper<>();
        if (StringUtils.isNotNull(name)) {
            queryWrapper.like(TrafficEvaluationType::getName, name);
        }
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 检验评价指标名称是否重复
     * @param trafficEvaluationType
     * @return
     */
    public boolean checkEvaluationNameUnique(TrafficEvaluationType trafficEvaluationType) {
        Long evaluationId = StringUtils.isNull(trafficEvaluationType.getId()) ? -1L : trafficEvaluationType.getId();
        LambdaQueryWrapper<TrafficEvaluationType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(TrafficEvaluationType::getName, trafficEvaluationType.getName());
        TrafficEvaluationType evaluationType = baseMapper.selectOne(queryWrapper);
        if (StringUtils.isNotNull(evaluationType) && evaluationType.getId().longValue() != evaluationId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}
